package com.example.demo.ai.service;

import com.example.demo.ai.AppConstants;
import com.example.demo.ai.dto.GetModelsResDto;
import com.example.demo.ai.dto.SyncDto;
import com.example.demo.ai.dto.assistant.CreateAssistantReqDto;
import com.example.demo.ai.dto.assistant.CreateAssistantResDto;
import com.example.demo.ai.dto.assistant.GetAssistantResDto;
import com.example.demo.ai.dto.thread.CreateThreadResDto;
import com.example.demo.ai.dto.thread.DeleteThreadResDto;
import com.example.demo.ai.entity.Assistant;
import com.example.demo.ai.entity.AssistantThread;
import com.example.demo.ai.entity.User;
import com.example.demo.ai.repo.AssistantRepo;
import com.example.demo.ai.repo.AssistantThreadRepo;
import com.example.demo.ai.repo.UserRepo;
import com.example.demo.ai.service.dto.DeleteAssistantResDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
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
    private final UserRepo userRepo;
    private final AssistantRepo assistantRepo;
    private final AssistantThreadRepo assistantThreadRepo;
    private final ObjectMapper objectMapper;

    @Qualifier("gptAssistantRestClient")
    private final RestClient restClient;

    GptAssistantCoreService(
            @Qualifier("gptAssistantRestClient") RestClient restClient,
            ObjectMapper objectMapper,
            UserRepo userRepo,
            AssistantRepo assistantRepo,
            AssistantThreadRepo assistantThreadRepo
    ) {
        this.userRepo = userRepo;
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

    public ResponseEntity<String> getThread(String threadId) {
        String url = "/v1/threads/" + threadId;
        return restClient.get()
                .uri(url)
                .retrieve()
                .toEntity(String.class);
    }


    /**
     * <p>어시스턴트 core 메서드 입니다.</p>
     *
     * @param instructions 어시스턴트 생성시 어떤 역할을 수행할지 자연어로 작성
     * @param name         어시스턴트의 이름 입니다.
     * @param model        어시스턴트의 기반 모델 ex) gpt-3.5
     */


    /**
     * <p>어시스턴트 정보를 DB에 저장하는 메서드 입니다.</p>
     */

    private CreateAssistantResDto createAssistantAPI(String instructions, String name, String model) {
        String url = "/v1/assistants";
        CreateAssistantReqDto dto = CreateAssistantReqDto.builder()
                .instructions(instructions)
                .name(name)
                .model(model)
                .build();

        ResponseEntity<String> jsonResponse = restClient.post()
                .uri(url)
                .body(dto)
                .retrieve()
                .toEntity(String.class);

        if (!jsonResponse.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Failed to create assistant - Open AI API: " + jsonResponse.getStatusCode());
        }

        try {
            return objectMapper.readValue(jsonResponse.getBody(), CreateAssistantResDto.class);
        } catch (JsonProcessingException e) {
            log.error("JSON Serialization Error: " + e.getMessage());
            throw new RuntimeException("JSON Serialization Exception - createAssistant");
        } catch (Exception e) {
            log.error("Failed to create assistant - Exception occurred during the process: " + e.getMessage());
            throw new RuntimeException("Exception during assistant creation: " + e.getMessage());
        }
    }

    public Assistant createAssistantDB(String instructions, String name, String model, String version, boolean isActive
    ) {
        Optional<Assistant> assistant = assistantRepo.findAssistantByName(name);
        if (assistant.isEmpty()) {
            return assistantRepo.save(
                    Assistant.builder()
                            .name(name)
                            .isActive(isActive)
                            .version(version)
                            .isDelete(false)
                            .instructions(instructions)
                            .model(model)
                            .build()
            );
        }
        return null;
    }

    private Assistant updatedAssistantIdDB(Assistant assistant, String assistantId) {
        System.out.println(assistant.getId() + "IDID");
        Optional<Assistant> assistantEntity = assistantRepo.findAssistantByName(assistant.getName());

        System.out.println(assistant.getName() + "GetNAme");
        System.out.println(assistantEntity.isPresent() + "IsPresent");
        if (assistantEntity.isPresent()) {

            return assistantRepo.save(Assistant.builder()
                    .id(assistant.getId())
                    .assistantId(assistantId)
                    .instructions(assistant.getInstructions())
                    .name(assistant.getName())
                    .version(assistant.getVersion())
                    .model(assistant.getModel())
                    .isActive(assistant.getIsActive())
                    .isDelete(assistant.getIsDelete())
                    .build());
        }
        return null;

    }


    /**
     * <p>스레드 생성 메서드</p>
     *
     * @param userId User 엔티티에서 유저를 식별할 고유 값을 의미합니다. 현재 임시 값이며 User 엔티티 구조에 따라 변경 될 수 있습니다.
     */
    @Transactional
    public CreateThreadResDto createThread(Long userId, String assistantId) {
        String url = "/v1/threads";
        Optional<AssistantThread> existingThread = assistantThreadRepo.findThreadByUserId(userId);

        if (existingThread.isPresent()) {
            return null;
        }

        User userEntity = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("존재하지 않는 유저"));
        Assistant assistant = assistantRepo.findAssistantByName(AppConstants.FATION_EXPERT_ASSISTANT_NAME + "_" + AppConstants.VERSION).orElseThrow(
                () -> new RuntimeException("어시스턴트를 찾을 수 없습니다.")
        );

        String jsonResponse = restClient
                .post()
                .uri(url)
                .retrieve()
                .body(String.class);

        String threadId = null;
        try {
            CreateThreadResDto createThreadResDto = objectMapper.readValue(jsonResponse, CreateThreadResDto.class);
            threadId = createThreadResDto.getId();

            assistantThreadRepo.save(AssistantThread.builder()
                    .user(userEntity)
                    .assistant(assistant)
                    .threadId(threadId)
                    .build());

            return createThreadResDto;
        } catch (JsonProcessingException e) {
            log.warn("----  Json Processing Exception  ----");
            log.info(jsonResponse + " : JsonResponse");
            log.warn("-------------------------------------");
            deleteThread(threadId);
            throw new RuntimeException("Json Processing Exception");
        } catch (Exception e) {
            deleteThread(threadId);
            throw new RuntimeException("Exception occurred during thread creation", e);
        }
    }

    /**
     * <p>어시스턴트 삭제 메서드 입니다. 같은 패키지 내에서도 사용할 수 없도록 private 접근 제한자 설정</p>
     */
    private void deleteAssistantAPI(String assistantId) {
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
            threadEntity.ifPresent(thread -> assistantThreadRepo.deleteById(thread.getId()));
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
     */
    @Transactional
    public SyncDto synchronizeAssistants() {
        List<GetAssistantResDto.Data> apiAssistants = getAssistants().getData();
        List<Assistant> dbAssistants = assistantRepo.findAll();

        Map<String, Assistant> dbAssistantMap = dbAssistants.stream()
                .collect(Collectors.toMap(Assistant::getAssistantId, Function.identity(), (existing, replacement) -> existing));

        SyncDto.SyncDtoBuilder syncResult = SyncDto.builder().syncCreated(false).syncDeleted(false);

        // 둘 다 없는 경우, 동기화 작업을 수행하지 않습니다.
        if (apiAssistants.isEmpty() && dbAssistants.isEmpty()) {
            log.info("No assistants found in both OpenAI and DB.");
            return syncResult.build();
        }

        // OpenAI의 API 어시스턴트 목록을 기반으로 필터링하여 DB에 없으면 삭제
        for (GetAssistantResDto.Data apiAssistant : apiAssistants) {
            if (!dbAssistantMap.containsKey(apiAssistant.getId())) {
                deleteAssistantAPI(apiAssistant.getId());
                log.info("Deleted orphaned assistant from OpenAI: " + apiAssistant.getName());
                syncResult.syncDeleted(true);
            }
        }

        // DB 어시스턴트 목록을 기반으로 필터링하여 OpenAI에 없으면 생성
        for (Assistant dbAssistant : dbAssistants) {
            if (apiAssistants.stream().noneMatch(apiAssistant -> apiAssistant.getId().equals(dbAssistant.getAssistantId()))) {
                String name = dbAssistant.getName();
                CreateAssistantResDto createdAssistant = createAssistantAPI(dbAssistant.getInstructions(), name, dbAssistant.getModel());
                updatedAssistantIdDB(dbAssistant, createdAssistant.getId());
                log.info("Created missing assistant in OpenAI: " + dbAssistant.getName());
                syncResult.syncCreated(true);
            }
        }


        return syncResult.build();
    }

    /**
     * <p>스레드 동기화 메서드 입니다. 각 유저들의 스레드를 조회하고 동기화 합니다.</p>
     */
    @Transactional
    public SyncDto synchronizeThread(Long userId, String assistantId) {
        Optional<AssistantThread> dbAssistantThread = assistantThreadRepo.findThreadByUserId(userId);
        if (dbAssistantThread.isEmpty()) {
            createThread(userId, assistantId);
            return SyncDto.builder()
                    .created(true)
                    .build();
        }
        log.info("Not Created - API and DB in Sync Success");
        return SyncDto.builder().build();
    }


    public void saveMessage() {
        // TODO: 생성된 메시지 정보를 저장 ( 보낸 메시지, 스레드 아이디 등 )
    }
}
