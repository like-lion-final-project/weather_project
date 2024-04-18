package com.example.demo.ai.service;

import com.example.demo.ai.AppConstants;
import com.example.demo.ai.dto.GetModelsResDto;
import com.example.demo.ai.dto.assistant.CreateAssistantReqDto;
import com.example.demo.ai.dto.assistant.CreateAssistantResDto;
import com.example.demo.ai.dto.assistant.GetAssistantResDto;
import com.example.demo.ai.dto.thread.DeleteThreadResDto;
import com.example.demo.ai.entity.Assistant;
import com.example.demo.ai.entity.AssistantThread;
import com.example.demo.ai.repo.AssistantRepo;
import com.example.demo.ai.repo.AssistantThreadRepo;
import com.example.demo.ai.service.dto.DeleteAssistantResDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>GPT Assistant api의 결과를 저장하고 라이프 사이클을 관리하는 서비스 입니다.</p>
 * <p>다른 패키지의 서비스에서 마음대로 사용할 수 없도록 패키지로 접근 제한</p>
 */
@Slf4j
@Service
class GptAssistantCoreService {
    private final AssistantRepo assistantRepo;
    private final AssistantThreadRepo assistantThreadRepo;
    private final ObjectMapper objectMapper;

    @Qualifier("gptAssistantRestClient")
    private final RestClient restClient;

    GptAssistantCoreService(
            @Qualifier("gptAssistantRestClient") RestClient restClient,
            ObjectMapper objectMapper,
            AssistantRepo assistantRepo,
            AssistantThreadRepo assistantThreadRepo
    ) {
        this.assistantRepo = assistantRepo;
        this.assistantThreadRepo = assistantThreadRepo;
        this.restClient = restClient;
        this.objectMapper = objectMapper;
    }

    /**
     * <p>현재 Chat GPT API 에서 사용 가능한 모델 정보를 반환하는 메서드</p>
     * <p>DEFAULT_MODEL_IDENTIFIER_LIST 변수에 저장된 기본 값들과 응답 값을 비교해서 교집합 데이터만 반환함</p>
     */
    public Set<String> getActiveModels() {
        String url = "/v1/models";
        String jsonResponse = restClient
                .get()
                .uri(url)
                .retrieve()
                .body(String.class);

        try {
            GetModelsResDto models = objectMapper.readValue(jsonResponse, GetModelsResDto.class);
            Set<String> activeModels = new HashSet<>();
            for (GetModelsResDto.Data model : models.getData()) {
                activeModels.add(model.getId());
            }

            activeModels.retainAll(AppConstants.DEFAULT_MODEL_IDENTIFIER_LIST);
            return activeModels;
        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage() + "JSON parsing error");
            throw new RuntimeException("JSON parsing error", e);
        }
    }

    /**
     * <p>타겟 모델이 사용가능한 상태인지 체크하는 메서드</p>
     *
     * @param modelName 모델의 이름입니다. ex) gpt-3.5-turbor-0613
     */
    public boolean isActiveTargetModel(String modelName) {
        Set<String> activeModels = getActiveModels(); // Use the getActiveModels method
        return activeModels.contains(modelName); // Check if the model is in the active models set
    }

    public GetAssistantResDto getAssistants() {
        String url = "v1/assistants";

        String jsonResponse = restClient
                .get()
                .uri(url)
                .retrieve()
                .body(String.class);

        try {
            return objectMapper.readValue(jsonResponse, GetAssistantResDto.class);
        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage() + "에러 메시지");
            throw new RuntimeException("json 가공 에러");
        }
    }


    /**
     * <p>어시스턴트 core 메서드 입니다.</p>
     *
     * @param instructions 어시스턴트 생성시 어떤 역할을 수행할지 자연어로 작성
     * @param name         어시스턴트의 이름 입니다.
     * @param model        어시스턴트의 기반 모델 ex) gpt-3.5
     */

    public CreateAssistantResDto createAssistant(String instructions, String name, String model) {
        List<GetAssistantResDto.Data> assistantResDto = getAssistants().getData();

        for (GetAssistantResDto.Data assistant : assistantResDto) {
            if(assistant.getName().equals(name)){
                throw new RuntimeException("이미 존재하는 어시스턴트 입니다.");
            }
        }

        String url = "/v1/assistants";
        CreateAssistantReqDto dto = CreateAssistantReqDto.builder()
                .instructions(instructions)
                .name(name)
                .model(model)
                .build();

        ResponseEntity<String> jsonResponse = restClient
                .post()
                .uri(url)
                .body(dto)
                .retrieve()
                .toEntity(String.class);

        if (!jsonResponse.getStatusCode().is2xxSuccessful())
            throw new RuntimeException("Failed to create assistant - Open AI API " + jsonResponse.getStatusCode());

        String assistantId = "";
        try {
            CreateAssistantResDto createAssistantResDto = objectMapper.readValue(jsonResponse.getBody(), CreateAssistantResDto.class);
            assistantId = createAssistantResDto.getId();

            assistantRepo.save(
                    Assistant.builder()
                            .name(createAssistantResDto.getName())
                            .assistantId(createAssistantResDto.getId())
                            .isActive(true)
                            .version(createAssistantResDto.getName().split("_")[1])
                            .isDelete(false)
                            .instructions(createAssistantResDto.getInstructions())
                            .model(createAssistantResDto.getModel())
                            .build()
            );
            return createAssistantResDto;
        } catch (JsonProcessingException e) {
            log.info("JSON 직렬화 에러");
            return null;
        } catch (Exception e){
            deleteAssistant(assistantId);
            throw new RuntimeException("Failed to created assistant - Application API");
        }
    }


    /**
     * <p>어시스턴트 삭제 메서드 입니다. 같은 패키지 내에서도 사용할 수 없도록 private 접근 제한자 설정</p>
     */
    private void deleteAssistant(String assistantId) {
        String url = "/v1/assistants/" + assistantId;
        String jsonResponse = restClient
                .delete()
                .uri(url)
                .retrieve()
                .body(String.class);

        try {
            DeleteAssistantResDto deleteAssistantResDto = objectMapper.readValue(jsonResponse, DeleteAssistantResDto.class);
            Optional<Assistant> assistantEntity = assistantRepo.findAssistantByAssistantId(deleteAssistantResDto.getId());
            assistantEntity.ifPresent(assistant -> assistantRepo.deleteById(assistant.getId()));
        } catch (JsonProcessingException e) {
            log.warn("Json Processing Exception - deleteAssistant");
            throw new RuntimeException("Json Processing Exception - deleteAssistant");
        } catch (Exception e) {
            log.warn("Runtime Exception");
            throw new RuntimeException("Exception - deleteAssistant");
        }
    }

    private void deleteThread(String threadId) {
        String url = "/v1/threads/" + threadId;

        try {
            String jsonResponse = restClient
                    .delete()
                    .uri(url)
                    .retrieve()
                    .body(String.class);

            DeleteThreadResDto deleteThreadResDto = objectMapper.readValue(jsonResponse, DeleteThreadResDto.class);
            Optional<AssistantThread> threadEntity = assistantThreadRepo.findThreadByThreadId(deleteThreadResDto.getId());
            threadEntity.ifPresent(thread -> assistantRepo.deleteById(thread.getId()));
        } catch (JsonProcessingException e) {
            log.warn("Json Processing Exception - deleteAssistant");
            throw new RuntimeException("Json Processing Exception - deleteAssistant");
        } catch (Exception e) {
            log.warn("Exception");
            throw new RuntimeException("Exception - deleteAssistant");
        }

    }

    /**
     * <p>어시스턴트 동기화 메서드 입니다.</p>
     * */
    public void synchronizeAssistants() {
        List<GetAssistantResDto.Data> apiAssistants = getAssistants().getData();
        List<Assistant> dbAssistants = assistantRepo.findAll();
        Map<String, Assistant> dbAssistantMap = dbAssistants.stream()
                .collect(Collectors.toMap(
                        Assistant::getAssistantId,  Function.identity()));

        for (GetAssistantResDto.Data apiAssistant : apiAssistants) {
            String key = apiAssistant.getId();
            if (!dbAssistantMap.containsKey(key)) {
                deleteAssistant(apiAssistant.getId());
                log.info("Deleted orphaned assistant from OpenAI: " + apiAssistant.getName());
            }
        }
    }


    public AssistantThread syncSaveThread(String threadId, String assistantId) {
        Optional<Assistant> assistant = assistantRepo.findAssistantByAssistantId(assistantId);

        if (assistant.isEmpty()) {
            log.warn("NotFound Assistant - ID: " + assistantId);
            throw new RuntimeException("NotFound Assistant - ID: " + assistantId);
        }

        try {
            return assistantThreadRepo.save(AssistantThread.builder()
                    .assistant(assistant.get())
                    .threadId(threadId)
                    .isDeleteFromOpenAi(false)
                    .build());
        } catch (DataAccessException e) {
            log.error("Database access error when saving assistant thread for ID: " + assistantId, e);
            deleteThread(threadId);
            throw new RuntimeException("Database error occurred while saving assistant thread");
        } catch (Exception e) {
            log.error("Unexpected error occurred", e);
            deleteThread(threadId);
            throw new RuntimeException("Unexpected error occurred", e);
        }
        // TODO: 생성된 스레드 정보를 저장 ( thread_id )
    }

    public void saveMessage() {
        // TODO: 생성된 메시지 정보를 저장 ( 보낸 메시지, 스레드 아이디 등 )
    }
}
