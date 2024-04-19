package com.example.demo.ai.service;

import com.example.demo.ai.AppConstants;
import com.example.demo.ai.dto.*;
import com.example.demo.ai.dto.assistant.CreateAssistantResDto;
import com.example.demo.ai.dto.assistant.GetAssistantResDto;
import com.example.demo.ai.dto.message.*;
import com.example.demo.ai.dto.run.CreateRunReqDto;
import com.example.demo.ai.dto.run.CreateRunResDto;
import com.example.demo.ai.dto.thread.CreateThreadReqDto;
import com.example.demo.ai.dto.thread.CreateThreadResDto;
import com.example.demo.ai.entity.Assistant;
import com.example.demo.ai.entity.AssistantThread;
import com.example.demo.ai.repo.AssistantRepo;
import com.example.demo.ai.repo.AssistantThreadRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * <p>Chat GPT Assistans와 통신하기 위한 서비스 입니다.</p>
 */
@Slf4j
@Service
@AllArgsConstructor
public class GptAssistantService {
    private final RestClient restClient;
    private final AssistantThreadRepo assistantThreadRepo;
    private final AssistantRepo assistantRepo;
    private final GptAssistantCoreService gptAssistantCoreService;

    @Autowired
    public GptAssistantService(@Qualifier("gptAssistantRestClient") RestClient restClient,
                               AssistantThreadRepo assistantThreadRepo,
                               AssistantRepo assistantRepo,
                               GptAssistantCoreService gptAssistantCoreService
    ) {
        this.restClient = restClient;
        this.assistantThreadRepo = assistantThreadRepo;
        this.assistantRepo = assistantRepo;
        this.gptAssistantCoreService = gptAssistantCoreService;
    }


    ObjectMapper objectMapper = new ObjectMapper();


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

    /**
     * <p>스레드를 생성하고 동시에 메시지를 포함한 실행요청을 보내는 메서드</p>
     *
     * @param assistantId 어시스턴트의 아이디 입니다.
     * @param messages    스레드에 담을 메시지 객체 리스트 입니다.
     */
    public CreateThreadAndRunResDto createThreadAndRun(String assistantId, List<CreateMessageDto> messages) {
        String url = "/v1/threads/runs";
        CreateThreadAndRunReqDto dto = CreateThreadAndRunReqDto.builder()
                .assistantId(assistantId)
                .thread(CreateThreadAndRunReqDto.Thread.builder().messages(messages).build()
                ).build();

        ResponseEntity<String> jsonResponse = restClient
                .post()
                .uri(url)
                .body(dto)
                .retrieve()
                .toEntity(String.class);

        try {
            return objectMapper.readValue(jsonResponse.getBody(), CreateThreadAndRunResDto.class);
        } catch (JsonProcessingException e) {
            System.out.println(e + "에러내용");
            log.warn("JSON Processing Exception - createdThreadAndRun");
            return null;
        }

    }

    /**
     * <p>어시스턴트 생성 및 동기화 메서드</p>
     *
     * @param instructions 어시스턴트 생성시 어떤 역할을 수행할지 자연어로 작성
     * @param name         어시스턴트의 이름 입니다.
     * @param model        어시스턴트의 기반 모델 ex) gpt-3.5
     */
    @Transactional
    public CreateAssistantResDto createAndSyncAssistant(String instructions, String name, String model) {

//        gptAssistantCoreService.synchronizeAssistants();

            return gptAssistantCoreService.createAssistant(instructions, name, model);

    }


    @Transactional
    public CreateThreadResDto createAndSyncThread(Long userId, String assistantId){
        gptAssistantCoreService.synchronizeThread(userId, assistantId);
      return gptAssistantCoreService.createThread(userId, assistantId);
    };



    /**
     * <p>어시스턴트 수정 메서드</p>
     */
    public void modifyAssistant() {
        // TODO: 어시스턴트 수정
        String url = "/v1/assistants";
    }




    public void getThreads() {
        // TODO: 스레드 목록 조회
    }

    public CreateMessageResDto createMessage(String threadId, String message) {
        // TODO: 스레드에 추가할 메시지 생성

        String url = "/v1/threads/" + threadId + "/messages";

        CreateMessageDto messageReqDto = CreateMessageDto.builder()
                .role("user")
                .content(message)
                .build();

        ResponseEntity<String> jsonResponse = restClient
                .post()
                .uri(url)
                .body(messageReqDto)
                .retrieve()
                .toEntity(String.class);

        try {
            return objectMapper.readValue(jsonResponse.getBody(), CreateMessageResDto.class);
        } catch (JsonProcessingException e) {
            log.info("JSON 직렬화 에러");
            return null;
        }

    }

    public GetMessagesResDto getMessages(String threadId) {
        String url = "/v1/threads/" + threadId + "/messages";

        String jsonResponse = restClient.get()
                .uri(url)
                .retrieve()
                .body(String.class);

        try {
            return objectMapper.readValue(jsonResponse, GetMessagesResDto.class);
        } catch (JsonProcessingException e) {
            System.out.println(e + " 에러내용");
            log.warn("Json 직렬화 에러");
            return null;
        }

    }

    public CreateRunResDto runAssistant(String threadId, String assistantId) {
        CreateRunReqDto createRunReqDto = CreateRunReqDto.builder()
                .assistantId(assistantId)
                .build();
        String url = "/v1/threads/" + threadId + "/runs";
        ResponseEntity<String> jsonResponse = restClient
                .post()
                .uri(url)
                .body(createRunReqDto)
                .retrieve()
                .toEntity(String.class);

        try {
            return objectMapper.readValue(jsonResponse.getBody(), CreateRunResDto.class);
        } catch (JsonProcessingException e) {
            log.info(e + " :Json 에러");
            return null;
        }

    }
}
