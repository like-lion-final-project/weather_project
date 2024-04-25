package com.example.demo.ai.service;

import com.example.demo.ai.dto.assistant.Assistant;
import com.example.demo.ai.dto.assistant.AssistantCreateRequest;
import com.example.demo.ai.dto.messages.Message;
import com.example.demo.ai.dto.messages.MessageList;
import com.example.demo.ai.dto.run.CreateThreadAndRunRequest;
import com.example.demo.ai.dto.run.Run;
import com.example.demo.ai.entity.AssistantEntity;
import com.example.demo.ai.entity.AssistantThread;
import com.example.demo.ai.entity.AssistantThreadMessage;
import com.example.demo.ai.repo.AssistantRepo;
import com.example.demo.ai.repo.AssistantThreadMessageRepo;
import com.example.demo.ai.repo.AssistantThreadRepo;
import com.example.demo.auth.AuthenticationFacade;
import com.example.demo.user.entity.User;
import com.example.demo.user.repo.UserRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Optional;

@Slf4j
@Service
public class GptAssistantApiService {
    private final AssistantThreadMessageRepo assistantThreadMessageRepo;
    private final UserRepo userRepo;
    private final RestClient restClient;
    private final AssistantThreadRepo assistantThreadRepo;
    private final AssistantRepo assistantRepo;
    private final ObjectMapper objectMapper;

    @Autowired
    public GptAssistantApiService(@Qualifier("gptAssistantClient") RestClient restClient,
                                  AssistantThreadRepo assistantThreadRepo,
                                  AssistantRepo assistantRepo,
                                  AuthenticationFacade authenticationFacade,
                                  ObjectMapper objectMapper,

                                  UserRepo userRepo,
                                  AssistantThreadMessageRepo assistantThreadMessageRepo) {
        this.restClient = restClient;
        this.assistantThreadRepo = assistantThreadRepo;
        this.assistantRepo = assistantRepo;
        this.objectMapper = objectMapper;
        this.userRepo = userRepo;
        this.assistantThreadMessageRepo = assistantThreadMessageRepo;
    }


    /**
     * <p>어시스턴트 생성 메서드 입니다.</p>
     */
    public Assistant createAssistantAPI(AssistantCreateRequest dto, String assistantType, String version) {
        if (!assistantType.equals("fashion")) {
            throw new RuntimeException("생성 불가능한 타입 입니다.");
        }


        Optional<AssistantEntity> assistant = assistantRepo.findAssistantByAssistantTypeAndVersion(assistantType, version);
        if (assistant.isPresent()) {
            throw new RuntimeException("이미 존재하는 어시스턴트 입니다.");
        }


        String uri = "/v1/assistants";
        ResponseEntity<String> json = restClient
                .post()
                .uri(uri)
                .body(AssistantCreateRequest.builder()
                        .instructions(dto.getInstructions())
                        .name(dto.getName())
                        .model(dto.getModel())
                        .tools(dto.getTools())
                        .toolsResources(dto.getToolsResources())
                        .responseFormat(dto.getResponseFormat())
                        .build())
                .retrieve()
                .toEntity(String.class);


        try {
            Assistant response = objectMapper.readValue(json.getBody(), Assistant.class);

            assistantRepo.save(AssistantEntity.builder()
                    .instructions(response.getInstructions())
                    .name(response.getName())
                    .version(response.getName().split("_")[1])
                    .model(response.getModel())
                    .assistantType(assistantType)
                    .assistantId(response.getId())
                    .isDeleteFromOpenAi(false)
                    .build());
            return response;
        } catch (JsonProcessingException e) {
            log.warn(e.getMessage());
            throw new RuntimeException("JsonProcessingException");
        } catch (DataAccessException e) {
            log.warn(e.getMessage());
            throw new RuntimeException("DB Exception");
        } catch (Exception e) {
            log.warn(e + "메시지");
            throw new RuntimeException("Exception");
        }

    }



    /**
     * <p>메시지 단일 조회 메서드 입니다.</p>
     * <p>조회할 때 스레드 아이디가 필요합니다. 스레드가 이미 삭제되었다면 해당 스레드에 포함되어있는 모든 메시지를 조회할 수 없습니다.</p>
     *
     * @param messageId 조회할 메시지의 아이디 입니다.
     */
    public Message getMessageAPI(String threadId, String messageId) {
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
            return objectMapper.readValue(json.getBody(), Message.class);
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
    public MessageList getMessagesAPI(String threadId) {
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
            return objectMapper.readValue(json.getBody(), MessageList.class);
        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("JsonProcessingException");
        } catch (Exception e) {
            throw new RuntimeException("Exception");
        }
    }



    /**
     * <p>실행 객체를 단일 조회 하는 메서드 입니다.</p>
     *
     * @param threadId 실행 객체에 포함된 스레드 아이디 입니다.
     * @param runId    실행객체의 아이디 입니다.
     */
    public Run getRun(String threadId, String runId) {
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
            return objectMapper.readValue(json.getBody(), Run.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JsonProcessingException");
        } catch (Exception e) {
            throw new RuntimeException("Exception");
        }
    }


    public Optional<AssistantThreadMessage> updateMessage(String runId, String newMessageUniqueId){
        Optional<AssistantThreadMessage> message = assistantThreadMessageRepo.findFirstByRunId(runId);
        return message.map(assistantThreadMessage -> assistantThreadMessageRepo.save(
                AssistantThreadMessage.builder()
                        .id(assistantThreadMessage.getId())
                        .messageId(newMessageUniqueId)
                        .object(assistantThreadMessage.getObject())
                        .assistantThread(assistantThreadMessage.getAssistantThread())
                        .role(assistantThreadMessage.getRole())
                        .runId(runId)
                        .type(assistantThreadMessage.getType())
                        .value(assistantThreadMessage.getValue())
                        .file_ids(assistantThreadMessage.getFile_ids())
                        .annotataions(assistantThreadMessage.getAnnotataions())
                        .metadata(assistantThreadMessage.getMetadata())
                        .isDeleteFromOpenAi(assistantThreadMessage.isDeleteFromOpenAi())
                        .build()));
    }

}
