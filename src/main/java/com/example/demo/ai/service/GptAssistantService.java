package com.example.demo.ai.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;


/**
 * <p>Chat GPT Assistans와 통신하기 위한 서비스 입니다.</p>
 * */
@Service
@RequiredArgsConstructor
public class GptAssistantService {
    private final RestClient rest;


    private static final String FATION_EXPERT_ASSISTANT_NAME = "Fashion Expert";
    private static final String[] MODEL_IDENTIFIER_LIST = {
            "gpt-3.5-turbo-16k-0613",
            "gpt-3.5-turbo-16k",
            "gpt-3.5-turbo-1106",
            "gpt-3.5-turbo-0613",
            "gpt-3.5-turbo-0125",
            "gpt-3.5-turbo"
    };


    public void createAssistant(String assistantName, String model){
        // TODO: 어시스턴트 생성
        String url = "/v1/assistants";

    }

    public Object getAssistants(){
        // TODO: 어시스턴트 목록 조회
        String url = "v1/assistants";
        return rest
                .get()
                .uri(url)
                .retrieve()
                .body(String.class);

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
