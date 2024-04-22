package com.example.demo.ai.service;

import com.example.demo.ai.dto.assistant.CreateAssistantReqDto;
import com.example.demo.ai.dto.assistant.CreateAssistantResDto;
import com.example.demo.ai.dto.assistant.GetAssistantResDto;
import com.example.demo.ai.repo.AssistantRepo;
import com.example.demo.ai.repo.AssistantThreadRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestClient;


/**
 * <p>Chat GPT Assistans와 통신하기 위한 서비스 입니다.</p>
 */
@Slf4j
@Service
public class GptAssistantApiService {
    private final RestClient restClient;
    private final AssistantThreadRepo assistantThreadRepo;
    private final AssistantRepo assistantRepo;
    private final ObjectMapper objectMapper;

    @Autowired
    public GptAssistantApiService(@Qualifier("gptAssistantRestClient") RestClient restClient,
                                  AssistantThreadRepo assistantThreadRepo,
                                  AssistantRepo assistantRepo,
                                  ObjectMapper objectMapper

    ) {
        this.restClient = restClient;
        this.assistantThreadRepo = assistantThreadRepo;
        this.assistantRepo = assistantRepo;
        this.objectMapper = objectMapper;
    }

    /**
     * <p>어시스턴트 생성 API 입니다.</p>
     * */
    public CreateAssistantResDto createAssistantAPI(
            CreateAssistantReqDto dto
    ){
        String uri = "/v1/assistants";
        ResponseEntity<String> json = restClient
                .post()
                .uri(uri)
                .body(dto)
                .retrieve()
                .toEntity(String.class);

        try{
            return objectMapper.readValue(json.getBody(),CreateAssistantResDto.class);
        }catch(JsonProcessingException e){
            throw new RuntimeException("JsonProcessingException");
        }catch (Exception e){
            throw new RuntimeException("Exception");
        }

    }

    // TODO: 어시스턴트 삭제
    public void deleteAssistantAPI(){}

    /**
     * <p>어시스턴트 단일 조회 메서드 입니다.</p>
     * @param assistantId 어시스턴트 아이디 입니다.
     * */
    public GetAssistantResDto.Data getAssistantAPI(String assistantId){
        String uri = "/v1/assistants/" + assistantId;
        ResponseEntity<String> json = restClient
                .get()
                .uri(uri)
                .retrieve()
                .toEntity(String.class);

        try{
            return objectMapper.readValue(json.getBody(),GetAssistantResDto.Data.class);
        }catch(JsonProcessingException e){
            throw new RuntimeException("JsonProcessingException");
        }catch (Exception e){
            throw new RuntimeException("Exception");
        }


    }



    /**
     * <p>어시스턴트 리스트 조회 메서드 입니다.</p>
     * */
    public GetAssistantResDto getAssistantsAPI(){
        String uri = "/v1/assistants";
        ResponseEntity<String> json = restClient
                .get()
                .uri(uri)
                .retrieve()
                .toEntity(String.class);

        try{
            return objectMapper.readValue(json.getBody(),GetAssistantResDto.class);
        }catch(JsonProcessingException e){
            throw new RuntimeException("JsonProcessingException");
        }catch (Exception e){
            throw new RuntimeException("Exception");
        }

    }

//    // TODO: 스레드 생성
//    public void createThreadAPI(){
//        String uri = "/"
//    }

    // TODO: 스레드 삭제
    public void deleteThreadAPI(){}

    // TODO: 메시지 생성
    public void createMessageAPI(String message){}

    // TODO: 메시지 조회
    public void getMessageAPI(String messageId){}

    // TODO: 메시지 리스트 보기
    public void getMessagesAPI(String threadId){}




}
