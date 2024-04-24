package com.example.demo.ai.service;

import com.example.demo.ai.dto.assistant.v2.Tool;
import com.example.demo.ai.dto.assistant.v2.ToolsResources;
import com.example.demo.ai.dto.messages.v2.messages.Message;
import com.example.demo.ai.dto.run.OneStepRunReqDto;
import com.example.demo.ai.dto.run.v2.Run;
import com.example.demo.ai.entity.Assistant;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;

@Service
public class GptAssistantApiServiceV2 {
    private final AssistantThreadMessageRepo assistantThreadMessageRepo;
    private final UserRepo userRepo;
    private final RestClient restClient;
    private final AssistantThreadRepo assistantThreadRepo;
    private final AssistantRepo assistantRepo;
    private final AuthenticationFacade authenticationFacade;
    private final ObjectMapper objectMapper;

    @Autowired
    public GptAssistantApiServiceV2(@Qualifier("gptAssistantRestClient") RestClient restClient,
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
        this.authenticationFacade = authenticationFacade;
        this.userRepo = userRepo;
        this.assistantThreadMessageRepo = assistantThreadMessageRepo;
    }
    /**
     * <p>스레드 생성과 메시지 추가 실행을 동시에 처리하는 메서드</p>
     *
     * @param assistantId 작업을 수행할 어시스턴트의 아이디
     * @param message     생성된 스레드에 포함할 단일 메시지
     * @param role        'user', 'assistant' 등 권한을 의미함. 기본 값은 user
     */
    public Run createThreadAndRun(String role, String message, String assistantId, List<Tool> tools, ToolsResources toolsResouces) {
        if (!role.equals("user") && !role.equals("assistant")) {
            role = "user";
        }
        List<Message> messages = new ArrayList<>();
        messages.add(Message.builder().role(role).content(message).build());

        OneStepRunReqDto.Thread reqThread = OneStepRunReqDto.Thread.builder().messages(messages).build();
        OneStepRunReqDto oneStepRunReqDto = OneStepRunReqDto.builder()
                .assistantId(assistantId)
                .thread(reqThread)
                .tools(tools)
                .toolsResources(toolsResouces)
                .build();

        String uri = "/v1/threads/runs";
        ResponseEntity<String> json = restClient
                .post()
                .uri(uri)
                .body(oneStepRunReqDto)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        (request, response) -> {
                            log.warn("에러내용: " + response.getStatusText());
                            throw new RuntimeException("Is4xx Client Error");
                        })
                .toEntity(String.class);

        Long tempUser = 1L;
        User user = userRepo.findById(tempUser).orElseThrow(() -> new RuntimeException("유저가 존재하지 않습니다."));
        Assistant assistant = assistantRepo.findAssistantByAssistantId(assistantId).orElseThrow(() -> new RuntimeException("어시스턴트가 존재하지 않습니다."));


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
                    .role(role)
                    .value(message)
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


}
