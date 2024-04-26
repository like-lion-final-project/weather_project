package com.example.demo.ai.service;

import com.example.demo.ai.dto.assistant.Assistant;
import com.example.demo.ai.dto.assistant.AssistantList;
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
import com.example.demo.ai.service.dto.DeleteAssistantResDto;
import com.example.demo.user.entity.User;
import com.example.demo.user.repo.UserRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class GptAssistantService {
    private final UserRepo userRepo;
    private final AssistantThreadRepo assistantThreadRepo;
    private final AssistantThreadMessageRepo assistantThreadMessageRepo;
    private final GptAssistantApiService gptAssistantApiService;
    private final AssistantRepo assistantRepo;
    private final ObjectMapper objectMapper;


    /**
     * <p>어시스턴트 생성 메서드 입니다.</p>
     */
    public Assistant createAssistant(AssistantCreateRequest dto, String assistantType, String version) {
        AssistantList assistantList = getAssistants();

        for ( Assistant item : assistantList.getAssistant() ) {
            if( item.getName().equals(dto.getName()) ){
                throw new RuntimeException("이미 존재하는 어시스턴트 입니다.");
            }
        }

        ResponseEntity<String> json = gptAssistantApiService.createAssistant(
                AssistantCreateRequest.builder()
                        .instructions(dto.getInstructions())
                        .name(dto.getName())
                        .model(dto.getModel())
                        .tools(dto.getTools())
                        .toolsResources(dto.getToolsResources())
                        .responseFormat(dto.getResponseFormat())
                        .build()
        );

        if(json.getStatusCode().is4xxClientError()){
            log.warn(json.getBody() + "");
            return null;
        }
        try {
            Assistant assistant = objectMapper.readValue(json.getBody(), Assistant.class);
            assistantRepo.save(
                    AssistantEntity.builder()
                            .assistantType(assistantType)
                            .name(dto.getName())
                            .version(version)
                            .versionLabel(version)
                            .model(dto.getModel())
                            .assistantId(assistant.getId())
                            .isDeleteFromOpenAi(false)
                            .build()

            );
            return assistant;
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
    };





    /**
     * <p>스레드 생성과 메시지 추가 실행을 동시에 처리하는 메서드</p>
     *
     * @param dto 요청 dto 입니다.
     */
    public Run createThreadAndRun(CreateThreadAndRunRequest dto) {

        ResponseEntity<String> json = gptAssistantApiService.createThreadAndRun(dto);
        if(json.getStatusCode().is4xxClientError()){
            log.warn(json.getBody() + "");
            return null;
        }

        Long tempUser = 1L;
        User user = userRepo.findById(tempUser).orElseThrow(() -> new RuntimeException("유저가 존재하지 않습니다."));
        AssistantEntity assistant = assistantRepo.findAssistantByAssistantId(dto.getAssistantId()).orElseThrow(() -> new RuntimeException("어시스턴트가 존재하지 않습니다."));


        try {
            Run response = objectMapper.readValue(json.getBody(), Run.class);

            AssistantThreadMessage.builder().build();
            AssistantThread thread = AssistantThread.builder()
                    .user(user)
                    .assistant(assistant)
                    .threadId(response.getThreadId())
                    .isDeleteFromOpenAi(false)
                    .build();
            assistantThreadRepo.save(thread);

            assistantThreadMessageRepo.save(AssistantThreadMessage.builder()
                    .object(response.getObject())
                    .assistantThread(thread)
                    .runId(response.getId())
                    .value(dto.getThread().getMessages().stream().findFirst().get().getContent())
                    .isDeleteFromOpenAi(false)
                    .build());

            return response;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JsonProcessingException");
        } catch (DataAccessException e) {
            System.out.println(e.getMessage() + "메시지");
            throw new RuntimeException("DB Exception");
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
    public Message getMessage(String threadId, String messageId) {
        String uri = "/v1/threads/" + threadId + "/messages/" + messageId;
        ResponseEntity<String> json = gptAssistantApiService.getMessage(threadId,messageId);

        if(json.getStatusCode().is4xxClientError()){
            log.warn(json.getBody() + "");
            return null;
        }
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
    public MessageList getMessages(String threadId) {
        String uri = "/v1/threads/" + threadId + "/messages";
        ResponseEntity<String> json = gptAssistantApiService.getMessages(threadId);
        if(json.getStatusCode().is4xxClientError()){
            log.warn(json.getBody() + "");
            return null;
        }
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
        ResponseEntity<String> json = gptAssistantApiService.getRuns(threadId,runId);
        if(json.getStatusCode().is4xxClientError()){
            log.warn(json.getBody() + "");
            return null;
        }
        try {
            return objectMapper.readValue(json.getBody(), Run.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JsonProcessingException");
        } catch (Exception e) {
            throw new RuntimeException("Exception");
        }
    }


    /**
     * <p>OpenAI에 저장된 어시스턴트를 불러오는 메서드 입니다.</p>
     * */
    public AssistantList getAssistants(){
        ResponseEntity<String> json = gptAssistantApiService.getAssistants();
        if(json.getStatusCode().is4xxClientError()){
            log.warn(json.getBody() + "");
            return null;
        }
        try {
            return objectMapper.readValue(json.getBody(),AssistantList.class);
        }catch (JsonProcessingException e){
            throw new RuntimeException("JsonProcessingException");
        }catch (Exception e){
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