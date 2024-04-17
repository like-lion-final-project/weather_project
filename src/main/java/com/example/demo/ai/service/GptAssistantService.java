package com.example.demo.ai.service;

import com.example.demo.ai.dto.CreateAssistantResDto;
import com.example.demo.ai.dto.GetAssistantResDto;
import com.example.demo.ai.dto.CreateAssistantReqDto;
import com.example.demo.ai.dto.GetModelsResDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


/**
 * <p>Chat GPT Assistans와 통신하기 위한 서비스 입니다.</p>
 * */
@Slf4j
@Service
public class GptAssistantService {
    private final RestClient restClient;

    @Autowired
    public GptAssistantService(@Qualifier("gptAssistantRestClient") RestClient restClient) {
        this.restClient = restClient;
    }


    ObjectMapper objectMapper = new ObjectMapper();



    private static final String FATION_EXPERT_ASSISTANT_NAME = "Fashion Expert";
    private static final Set<String> DEFAULT_MODEL_IDENTIFIER_LIST = new HashSet<>(Arrays.asList(
            "gpt-3.5-turbo-16k-0613", "gpt-3.5-turbo-16k", "gpt-3.5-turbo-1106",
            "gpt-3.5-turbo-0613", "gpt-3.5-turbo-0125", "gpt-3.5-turbo"
    ));



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

            activeModels.retainAll(DEFAULT_MODEL_IDENTIFIER_LIST); // Correct use of retainAll
            return activeModels; // Correct return
        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage() + "에러 메시지");
            throw new RuntimeException("JSON parsing error", e);
        }
    }

    public boolean isActiveTargetModel(String modelName) {
        Set<String> activeModels = getActiveModels(); // Use the getActiveModels method
        return activeModels.contains(modelName); // Check if the model is in the active models set
    }


    /**
     * <p>어시스턴트 생성 메서드</p>
     * */
    @Transactional
    public CreateAssistantResDto createAssistant(String instructions, String name, String model) {
        GetAssistantResDto assistantResDto = getAssistants();
        boolean isExistAssistant = false;

        for (GetAssistantResDto.Data data : assistantResDto.getData()) {
            isExistAssistant = data.getName().equals(FATION_EXPERT_ASSISTANT_NAME);
        }

        // 존재하는 어시스턴트 인지 체크
        if(isExistAssistant){
            log.info("이미 생성된 어시스턴트 입니다.");
            return null;
        }

        // 존재하는 모델인지 체크
        if(!isActiveTargetModel(model)){
            log.info("모델이 존재하지 않습니다.");
            return null;
        }

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
     * */
    public GetAssistantResDto getAssistants() {
        String url = "v1/assistants";

        String jsonResponse = restClient
                .get()
                .uri(url)
                .retrieve()
                .body(String.class);

        try {
            return objectMapper.readValue(jsonResponse, GetAssistantResDto.class);
        }catch (JsonProcessingException e) {
            System.out.println(e.getMessage() + "에러 메시지");
            throw new RuntimeException("json 가공 에러");
        }


    }


    /**
     * <p>어시스턴트 수정 메서드</p>
     * */
    public void modifyAssistant(){
        // TODO: 어시스턴트 수정
        String url = "/v1/assistants";
    }

    public void createThread(Integer userId){
        // TODO: 스레드 생성
        // TODO: 유저마다 하나의 스레드를 생성함
        // TODO: 유저의 스레드 정보를 DB에 기록하고 삭제 요청시 삭제함
        String url = "/v1/threads";


        String jsonResponse = restClient
                .post()
                .uri(url)
                .retrieve()
                .body(String.class);

        System.out.println(jsonResponse + ":json response");
    }

    public void createMessage(String threadId){
        // TODO: 스레드에 추가할 메시지 생성
        String url = "/v1/assistants";
    }

    public void runAssistant(String threadId, String assistantId){
        // TODO: 어시스턴트에 요청
        String url = "/v1/assistants";
    }
}
