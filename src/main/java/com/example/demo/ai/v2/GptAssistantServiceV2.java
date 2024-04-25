package com.example.demo.ai.v2;

import com.example.demo.ai.dto.assistant.Assistant;
import com.example.demo.ai.dto.assistant.AssistantCreateRequest;
import com.example.demo.ai.dto.run.CreateThreadAndRunRequest;
import com.example.demo.ai.dto.run.Run;
import com.example.demo.ai.entity.AssistantEntity;
import com.example.demo.ai.entity.AssistantThread;
import com.example.demo.ai.entity.AssistantThreadMessage;
import com.example.demo.ai.repo.AssistantRepo;
import com.example.demo.ai.repo.AssistantThreadMessageRepo;
import com.example.demo.ai.repo.AssistantThreadRepo;
import com.example.demo.ai.v2.GptAssistantApiServiceV2;
import com.example.demo.user.entity.User;
import com.example.demo.user.repo.UserRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class GptAssistantServiceV2 {
    private final UserRepo userRepo;
    private final AssistantThreadRepo assistantThreadRepo;
    private final AssistantThreadMessageRepo assistantThreadMessageRepo;
    private final GptAssistantApiServiceV2 gptAssistantApiServiceV2;
    private final AssistantRepo assistantRepo;
    private final ObjectMapper objectMapper;


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


        ResponseEntity<String> json = gptAssistantApiServiceV2.createAssistant(
                AssistantCreateRequest.builder()
                        .instructions(dto.getInstructions())
                        .name(dto.getName())
                        .model(dto.getModel())
                        .tools(dto.getTools())
                        .toolsResources(dto.getToolsResources())
                        .responseFormat(dto.getResponseFormat())
                        .build()
        );



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
     * <p>스레드 생성과 메시지 추가 실행을 동시에 처리하는 메서드</p>
     *
     * @param dto 요청 dto 입니다.
     */
    public Run createThreadAndRun(CreateThreadAndRunRequest dto) {


        String uri = "/v1/threads/runs";
        ResponseEntity<String> json = gptAssistantApiServiceV2.createThreadAndRun(dto);

//                restClient
//                .post()
//                .uri(uri)
//                .body(dto)
//                .retrieve()
//                .onStatus(HttpStatusCode::is4xxClientError,
//                        (request, response) -> {
//                            log.warn(response.toString());
//
//                            log.warn("에러내용: " + response.getStatusText());
//                            log.warn("에러내용: " + response.getBody());
//                            throw new RuntimeException("Is4xx Client Error");
//                        })
//                .toEntity(String.class);

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



}