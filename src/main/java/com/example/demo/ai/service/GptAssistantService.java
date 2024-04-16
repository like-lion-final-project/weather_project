package com.example.demo.ai.service;

import org.springframework.stereotype.Service;


/**
 * <p>Chat GPT Assistans와 통신하기 위한 서비스 입니다.</p>
 * */
@Service
public class GptAssistantService {
    /**
     * OpenAI assistant api 헤더에 추가할 베타 표시.
     */
    public static final String OPEN_AI_BETA = "OpenAI-Beta";
    /**
     * OpenAI assistant api 버전.
     */
    public static final String ASSISTANTS_V1 = "assistants=v1";
    private static final String BASE_URL = "https://api.openai.com";

    /**
     * 1. 어시스턴트 생성 ( UI에서 이미 만들었음 )
     * 2. Thread 생성
     * 3. Thread에 Message 생성
     * 4. Run 생성 ( Thread에 추가된 Message에 대해  Assistants의 응답을 받아옴 )
     * */


    private void createAssistant(){
        // TODO: 어시스턴트 생성
    }


    private void modifyAssistant(){
        // TODO: 어시스턴트 수정
    }

    public void createThread(){
        // TODO: 스레드 생성
    }

    public void createMessage(String threadId){
        // TODO: 스레드에 추가할 메시지 생성
    }

    public void runAssistant(String threadId, String assistantId){
        // TODO: 어시스턴트에 요청
    }
}
