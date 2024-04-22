package com.example.demo.ai.service;

import com.example.demo.ai.repo.AssistantRepo;
import com.example.demo.ai.repo.AssistantThreadRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;


/**
 * <p>Chat GPT Assistans와 통신하기 위한 서비스 입니다.</p>
 */
@Slf4j
@Service
class GptAssistantCoreService {
    private final RestClient restClient;
    private final AssistantThreadRepo assistantThreadRepo;
    private final AssistantRepo assistantRepo;

    @Autowired
    public GptAssistantCoreService(@Qualifier("gptAssistantRestClient") RestClient restClient,
                                   AssistantThreadRepo assistantThreadRepo,
                                   AssistantRepo assistantRepo

    ) {
        this.restClient = restClient;
        this.assistantThreadRepo = assistantThreadRepo;
        this.assistantRepo = assistantRepo;
    }

    // TODO: 어시스턴트 생성
    public void createAssistant(){}

    // TODO: 어시스턴트 삭제
    public void deleteAssistant(){}

    // TODO: 어시스턴트 조회
    public void getAssistant(String assistantId){}

    // TODO: 어시스턴트 리스트 보기
    public void getAssistants(){}

    // TODO: 스레드 생성
    public void createThread(){}

    // TODO: 스레드 삭제
    public void deleteThread(){}

    // TODO: 메시지 생성
    public void createMessage(String message){}

    // TODO: 메시지 조회
    public void getMessage(String messageId){}

    // TODO: 메시지 리스트 보기
    public void getMessages(String threadId){}




}
