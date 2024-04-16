package com.example.demo.ai.service;

import com.example.demo.ai.dto.CreateAssistantResDto;
import com.example.demo.ai.dto.GetAssistantResDto;
import com.example.demo.ai.dto.CreateAssistantReqDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;


/**
 * <p>Chat GPT Assistans와 통신하기 위한 서비스 입니다.</p>
 * */
@Service
public class GptAssistantService {
    private final RestClient restClient;

    @Autowired
    public GptAssistantService(@Qualifier("gptAssistantRestClient") RestClient restClient) {
        this.restClient = restClient;
    }


    ObjectMapper objectMapper = new ObjectMapper();



    private static final String FATION_EXPERT_ASSISTANT_NAME = "Fashion Expert";
    private String[] DEFAULT_MODEL_IDENTIFIER_LIST = {
            "gpt-3.5-turbo-16k-0613",
            "gpt-3.5-turbo-16k",
            "gpt-3.5-turbo-1106",
            "gpt-3.5-turbo-0613",
            "gpt-3.5-turbo-0125",
            "gpt-3.5-turbo"
    };


    public boolean getActiveModels(){
        String url = "/v1/models";

        return true;
    }

    public boolean isActiveTargetModel(String model){
        return true;
    }


    /**
     * <p>어시스턴트 생성 메서드</p>
     * */
    @Transactional
    public CreateAssistantResDto createAssistant(String instructions, String name, String model) {
        GetAssistantResDto assistantResDto = getAssistants();
        boolean isExist = false;


        for (GetAssistantResDto.Data data : assistantResDto.getData()) {
            isExist = data.getName().equals(FATION_EXPERT_ASSISTANT_NAME);
        }

        if(isExist){
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
            System.out.println(e + " : json 에러");
            return null;
        }

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
        }catch (JsonProcessingException e) {
            System.out.println(e.getMessage() + "에러 메시지");
            throw new RuntimeException("json 가공 에러");
        }


    }


    public void modifyAssistant(){
        // TODO: 어시스턴트 수정
        String url = "/v1/assistants";
    }

    public void createThread(){
        // TODO: 스레드 생성
        String url = "/v1/assistants";
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
