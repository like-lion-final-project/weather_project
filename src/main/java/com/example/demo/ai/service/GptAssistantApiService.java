package com.example.demo.ai.service;

import com.example.demo.ai.dto.assistant.CreateAssistantReqDto;
import com.example.demo.ai.dto.assistant.CreateAssistantResDto;
import com.example.demo.ai.dto.assistant.GetAssistantResDto;
import com.example.demo.ai.dto.message.CreateMessageDto;
import com.example.demo.ai.dto.message.CreateMessageResDto;
import com.example.demo.ai.dto.message.GetMessagesResDto;
import com.example.demo.ai.dto.thread.CreateThreadResDto;
import com.example.demo.ai.dto.thread.DeleteThreadResDto;
import com.example.demo.ai.repo.AssistantRepo;
import com.example.demo.ai.repo.AssistantThreadRepo;
import com.example.demo.ai.service.dto.DeleteAssistantResDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
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
     * <p>어시스턴트 생성 메서드 입니다.</p>
     */
    public CreateAssistantResDto createAssistantAPI(CreateAssistantReqDto dto) {
        String uri = "/v1/assistants";
        ResponseEntity<String> json = restClient
                .post()
                .uri(uri)
                .body(dto)
                .retrieve()
                .toEntity(String.class);

        try {
            return objectMapper.readValue(json.getBody(), CreateAssistantResDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JsonProcessingException");
        } catch (Exception e) {
            throw new RuntimeException("Exception");
        }

    }


    /**
     * <p>어시스턴트 삭제 메서드 입니다.</p>
     *
     * @param assistantId 삭제할 어시스턴트의 아이디 입니다.
     */
    public DeleteAssistantResDto deleteAssistantAPI(String assistantId) {
        String uri = "/v1/assistants/" + assistantId;
        ResponseEntity<String> json = restClient
                .delete()
                .uri(uri)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        (request, response) -> {
                            log.warn("에러 메시지: " + response.getStatusText());
                            throw new RuntimeException("Is4xx Client Error");
                        })
                .toEntity(String.class);

        try {
            return objectMapper.readValue(json.getBody(), DeleteAssistantResDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JsonProcessingException");
        } catch (Exception e) {
            throw new RuntimeException("Exception");
        }
    }


    /**
     * <p>어시스턴트 단일 조회 메서드 입니다.</p>
     *
     * @param assistantId 어시스턴트 아이디 입니다.
     */
    public GetAssistantResDto.Data getAssistantAPI(String assistantId) {
        String uri = "/v1/assistants/" + assistantId;
        ResponseEntity<String> json = restClient
                .get()
                .uri(uri)
                .retrieve()
                .toEntity(String.class);

        try {
            return objectMapper.readValue(json.getBody(), GetAssistantResDto.Data.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JsonProcessingException");
        } catch (Exception e) {
            throw new RuntimeException("Exception");
        }


    }


    /**
     * <p>어시스턴트 리스트 조회 메서드 입니다.</p>
     */
    public GetAssistantResDto getAssistantsAPI() {
        String uri = "/v1/assistants";
        ResponseEntity<String> json = restClient
                .get()
                .uri(uri)
                .retrieve()
                .toEntity(String.class);

        try {
            return objectMapper.readValue(json.getBody(), GetAssistantResDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JsonProcessingException");
        } catch (Exception e) {
            throw new RuntimeException("Exception");
        }

    }


    /**
     * <p>스레드 생성 메서드 입니다.</p>
     */
    public CreateThreadResDto createThreadAPI() {
        String uri = "/v1/threads";
        ResponseEntity<String> json = restClient
                .post()
                .uri(uri)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        (request, response) -> {
                            log.warn("에러메시지: " + response.getStatusText());
                            throw new RuntimeException("Is4xx Client Error");
                        })
                .toEntity(String.class);

        try {
            return objectMapper.readValue(json.getBody(), CreateThreadResDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JsonProcessingException");
        } catch (Exception e) {
            throw new RuntimeException("Exception");
        }
    }


    /**
     * <p>스레드 삭제 메서드 입니다.</p>
     *
     * @param threadId 삭제할 스레드 아이디 입니다.
     */
    public DeleteThreadResDto deleteThreadAPI(String threadId) {
        String uri = "/v1/threads/" + threadId;
        ResponseEntity<String> json = restClient
                .delete()
                .uri(uri)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        (request, response) -> {
                            log.warn("에러내용: " + response.getStatusText());
                            throw new RuntimeException("Is4xx Client Error");
                        })
                .toEntity(String.class);

        try {
            return objectMapper.readValue(json.getBody(), DeleteThreadResDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JsonProcessingException");
        } catch (Exception e) {
            throw new RuntimeException("Exception");
        }
    }

    /**
     * <p>메시지 생성 메서드 입니다.</p>
     *
     * @param message 전송할 단일 메시지 입니다.
     */
    public CreateMessageResDto createMessageAPI(String role, String message, String threadId) {
        String uri = "/v1/threads/" + threadId + "/messages";

        ResponseEntity<String> json = restClient
                .post()
                .uri(uri)
                .body(
                        CreateMessageDto.builder()
                                .content(message)
                                .role(role)
                                .build()
                ).retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        (request, response) -> {
                            log.warn("에러내용: " + response.getStatusText());
                            throw new RuntimeException("Is4xx Client Error");
                        })
                .toEntity(String.class);

        try {
            return objectMapper.readValue(json.getBody(), CreateMessageResDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JsonProcessingException");
        } catch (Exception e) {
            throw new RuntimeException("Exception");
        }
    }


    /**
     * <p>메시지 단일 조회 메서드 입니다.</p>
     * <p>조회할 때 스레드 아이디가 필요합니다. 스레드가 이미 삭제되었다면 해당 스레드에 포함되어있는 모든 메시지를 조회할 수 없습니다.</p>
     * @param messageId 조회할 메시지의 아이디 입니다.
     * */
    public GetMessagesResDto getMessagesAPI(String threadId, String messageId) {
        String uri = "/v1/threads/" + threadId + "/messages/" + messageId;
        ResponseEntity<String> json = restClient
                .get()
                .uri(uri)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        (request, response) -> {
                            log.warn("에러내용: " + response.getStatusText());
                            throw new RuntimeException("Is4xx Client Error");
                        })
                .toEntity(String.class);
        try {
            return objectMapper.readValue(json.getBody(), GetMessagesResDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JsonProcessingException");
        } catch (Exception e) {
            throw new RuntimeException("Exception");
        }
    }

    // TODO: 메시지 리스트 보기
    public void getMessagesAPI(String threadId) {
    }
}
