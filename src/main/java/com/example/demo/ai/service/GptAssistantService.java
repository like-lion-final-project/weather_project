package com.example.demo.ai.service;

import com.example.demo.ai.AppConstants;
import com.example.demo.ai.dto.*;
import com.example.demo.ai.dto.assistant.CreateAssistantReqDto;
import com.example.demo.ai.dto.assistant.CreateAssistantResDto;
import com.example.demo.ai.dto.assistant.GetAssistantResDto;
import com.example.demo.ai.dto.message.CreateMessageReqDto;
import com.example.demo.ai.dto.message.CreateMessageResDto;
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
import java.util.Set;


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

    @Autowired
    public GptAssistantService(@Qualifier("gptAssistantRestClient") RestClient restClient, AssistantThreadRepo assistantThreadRepo, AssistantRepo assistantRepo) {
        this.restClient = restClient;
        this.assistantThreadRepo = assistantThreadRepo;
        this.assistantRepo = assistantRepo;
    }


    ObjectMapper objectMapper = new ObjectMapper();





    /**
     * <p>현재 Chat GPT API 에서 사용 가능한 모델 정보를 반환하는 메서드</p>
     * <p>DEFAULT_MODEL_IDENTIFIER_LIST 변수에 저장된 기본 값들과 응답 값을 비교해서 교집합 데이터만 반환함</p>
     *
     * */
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
     * @param modelName 모델의 이름입니다. ex) gpt-3.5-turbor-0613
     * */
    public boolean isActiveTargetModel(String modelName) {
        Set<String> activeModels = getActiveModels(); // Use the getActiveModels method
        return activeModels.contains(modelName); // Check if the model is in the active models set
    }


    /**
     * <p>어시스턴트 생성 메서드</p>
     * @param instructions 어시스턴트 생성시 어떤 역할을 수행할지 자연어로 작성
     * @param name 어시스턴트의 이름 입니다.
     * @param model 어시스턴트의 기반 모델 ex) gpt-3.5
     */
    @Transactional
    public CreateAssistantResDto createAssistant(String instructions, String name, String model) {
        GetAssistantResDto assistantResDto = getAssistants();
        boolean isExistAssistant = false;

        for (GetAssistantResDto.Data data : assistantResDto.getData()) {
            isExistAssistant = data.getName().equals(AppConstants.FATION_EXPERT_ASSISTANT_NAME);
        }

        // 존재하는 어시스턴트 인지 체크
        if (isExistAssistant) {
            log.info("이미 생성된 어시스턴트 입니다.");
            return null;
        }

        // 존재하는 모델인지 체크
        if (!isActiveTargetModel(model)) {
            log.info("모델이 존재하지 않습니다.");
            return null;
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


        if (!jsonResponse.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Failed to create assistant with status: " + jsonResponse.getStatusCode());
        }

        try {
            return objectMapper.readValue(jsonResponse.getBody(), CreateAssistantResDto.class);
        } catch (JsonProcessingException e) {
            log.info("JSON 직렬화 에러");
            return null;
        }

    }

    /**
     * <p>어시스턴트 목록 조회 메서드</p>
     */
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
     * <p>어시스턴트 수정 메서드</p>
     */
    public void modifyAssistant() {
        // TODO: 어시스턴트 수정
        String url = "/v1/assistants";
    }


    /**
     * <p>스레드 생성 메서드</p>
     * @param userId User 엔티티에서 유저를 식별할 고유 값을 의미합니다. 현재 임시 값이며 User 엔티티 구조에 따라 변경 될 수 있습니다.
     * */
    @Transactional
    public AssistantThread createThread(Integer userId) {
        // TODO: 유저마다 하나의 스레드를 생성함
        // TODO: 유저의 스레드 정보를 DB에 기록하고 삭제 요청시 삭제함
        String url = "/v1/threads";

        Assistant assistant = assistantRepo.findAssistantByName(AppConstants.FATION_EXPERT_ASSISTANT_NAME + "_" +AppConstants.VERSION).orElseThrow(
                () -> new RuntimeException("어시스턴트를 찾을 수 없습니다.")
        );

        // 유저 아이디 혹은 유저네임을 통해 생성된 스레드가 있는지 체크
        if(true){
            // userRepo.findBy ...
        }

        String jsonResponse = restClient
                .post()
                .uri(url)
                .retrieve()
                .body(String.class);

        try {
            CreateThreadResDto createThreadResDto = objectMapper.readValue(jsonResponse, CreateThreadResDto.class);
            AssistantThread assistantThreadEntity = AssistantThread.builder()
                    .assistant(assistant)
                    .name(createThreadResDto.getId())
                    .build();
            return assistantThreadRepo.save(assistantThreadEntity);
        } catch (JsonProcessingException e) {
            log.info(e + " :Json 에러");
        }
        System.out.println(jsonResponse + ":json response");
        return null;
    }

    public void getThreads() {
        // TODO: 스레드 목록 조회
    }

    public CreateMessageResDto createMessage(String threadId, String message) {
        // TODO: 스레드에 추가할 메시지 생성

        String url = "/v1/threads/" + threadId + "/messages";

        CreateMessageReqDto messageReqDto = CreateMessageReqDto.builder()
                .role("user")
                .content(message)
                .build();

        ResponseEntity<String> jsonResponse = restClient
                .post()
                .uri(url)
                .body(messageReqDto)
                .retrieve()
                .toEntity(String.class);

        try{
            return objectMapper.readValue(jsonResponse.getBody(), CreateMessageResDto.class);
        }catch(JsonProcessingException e){
            log.info("JSON 직렬화 에러");
            return null;
        }

    }

    public void runAssistant(String threadId, String assistantId) {
        // TODO: 어시스턴트에 요청
        String url = "/v1/assistants";
    }
}
