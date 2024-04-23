package com.example.demo.ai.service;

import com.example.demo.ai.dto.assistant.CreateAssistantReqDto;
import com.example.demo.ai.dto.assistant.CreateAssistantResDto;
import com.example.demo.ai.dto.assistant.GetAssistantResDto;
import com.example.demo.ai.dto.message.CreateMessageDto;
import com.example.demo.ai.dto.message.CreateMessageResDto;
import com.example.demo.ai.dto.message.GetMessagesResDto;
import com.example.demo.ai.dto.run.CreateRunReqDto;
import com.example.demo.ai.dto.run.CreateRunResDto;
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
     *
     * @param messageId 조회할 메시지의 아이디 입니다.
     */
    public GetMessagesResDto getMessageAPI(String threadId, String messageId) {
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


    /**
     * <p>메시지 리스트 조회 메서드 입니다.</p>
     * <p>조회할 때 스레드 아이디가 필요합니다. 스레드가 이미 삭제되었다면 해당 스레드에 포함되어있는 모든 메시지를 조회할 수 없습니다.</p>
     *
     * @param threadId 조회할 메시지들이 들어있는 스레드 아이디
     */
    public GetMessagesResDto getMessagesAPI(String threadId) {
        String uri = "/v1/threads/" + threadId + "/messages";
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


    /**
     * <p>실행 메서드 입니다.</p>
     * <p>결과가 비동기로 처리됩니다. 일정주기로 결과를 확인하는 요청을 보내거나 메시지큐 서비스로 처리해야 합니다.</p>
     * @param threadId 실행 객체에 포함할 스레드 아이디 입니다. 해당 스레드 내에 있는 모든 메시지를 포함합니다.
     * @param assistantId 실행시 어떤 어시스턴트가 해당 작업을 수행할지 결정하는 어시스턴트 아이디 입니다.
     */
    public CreateRunResDto run(String threadId, String assistantId) {
        String uri = "/v1/threads/" + threadId + "/runs";
        ResponseEntity<String> json = restClient
                .post()
                .uri(uri)
                .body(CreateRunReqDto.builder()
                        .assistantId(assistantId)
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        (request, response) -> {
                            log.warn("에러내용: " + response.getStatusText());
                            throw new RuntimeException("Is4xx Client Error");
                        })
                .toEntity(String.class);

        try {
            return objectMapper.readValue(json.getBody(), CreateRunResDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JsonProcessingException");
        } catch (Exception e) {
            throw new RuntimeException("Exception");
        }
    }


    /**
     * <p>실행 객체를 단일 조회 하는 메서드 입니다.</p>
     * @param threadId 실행 객체에 포함된 스레드 아이디 입니다.
     * @param runId 실행객체의 아이디 입니다.
     * */
    public CreateRunResDto getRun(String threadId, String runId){
        String uri = "/v1/threads/" + threadId + "/runs/" + runId;
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
            return objectMapper.readValue(json.getBody(), CreateRunResDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JsonProcessingException");
        } catch (Exception e) {
            throw new RuntimeException("Exception");
        }
    }


    // TODO: 어시스턴트 생성시 DB에 히스토리 남김
    // TODO: 스레드 생성시 DB에 히스토리 남김
    // TODO: 메시지 생성시 DB에 히스토리 남김
    // TODO: 어시스턴트, 스레드 조회시 없는 항목이라면 is_delete true로 변경
    // TODO: DB에 저장된 어시스턴트 히스토리, 스레드 히스토리 조회시 is_delete false 인 항목만 조회하도록 구현
}
