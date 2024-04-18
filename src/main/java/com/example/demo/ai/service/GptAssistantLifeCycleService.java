package com.example.demo.ai.service;

import com.example.demo.ai.dto.assistant.CreateAssistantResDto;
import com.example.demo.ai.dto.thread.DeleteThreadResDto;
import com.example.demo.ai.entity.Assistant;
import com.example.demo.ai.entity.AssistantThread;
import com.example.demo.ai.repo.AssistantRepo;
import com.example.demo.ai.repo.AssistantThreadRepo;
import com.example.demo.ai.service.dto.DeleteAssistantResDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import javax.swing.text.html.Option;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Random;

/**
 * <p>GPT Assistant api의 결과를 저장하고 라이프 사이클을 관리하는 서비스 입니다.</p>
 * <p>다른 패키지의 서비스에서 마음대로 사용할 수 없도록 패키지로 접근 제한</p>
 */
@Slf4j
@Service
class GptAssistantLifeCycleService {
    private final AssistantRepo assistantRepo;
    private final AssistantThreadRepo assistantThreadRepo;
    private final ObjectMapper objectMapper;

    @Qualifier("gptAssistantRestClient")
    private final RestClient restClient;

    GptAssistantLifeCycleService(
            @Qualifier("gptAssistantRestClient") RestClient restClient,
            ObjectMapper objectMapper,
            AssistantRepo assistantRepo,
            AssistantThreadRepo assistantThreadRepo
    ) {
        this.assistantRepo = assistantRepo;
        this.assistantThreadRepo = assistantThreadRepo;
        this.restClient = restClient;
        this.objectMapper = objectMapper;
    }

    /**
     * <p>어시스턴트 삭제 메서드 입니다. 같은 패키지 내에서도 사용할 수 없도록 private 접근 제한자 설정</p>
     */
    private void deleteAssistant(String assistantId) {
        String url = "/v1/assistants/" + assistantId;
        String jsonResponse = restClient
                .delete()
                .uri(url)
                .retrieve()
                .body(String.class);

        try {
            DeleteAssistantResDto deleteAssistantResDto = objectMapper.readValue(jsonResponse, DeleteAssistantResDto.class);
            Optional<Assistant> assistantEntity = assistantRepo.findAssistantByAssistantId(deleteAssistantResDto.getId());
            assistantEntity.ifPresent(assistant -> assistantRepo.deleteById(assistant.getId()));
        } catch (JsonProcessingException e) {
            log.warn("Json Processing Exception - deleteAssistant");
            throw new RuntimeException("Json Processing Exception - deleteAssistant");
        } catch (Exception e) {
            log.warn("Runtime Exception");
            throw new RuntimeException("Exception - deleteAssistant");
        }
    }

    private void deleteThread(String threadId) {
        String url = "/v1/threads/" + threadId;

        try {
            String jsonResponse = restClient
                    .delete()
                    .uri(url)
                    .retrieve()
                    .body(String.class);

            DeleteThreadResDto deleteThreadResDto = objectMapper.readValue(jsonResponse, DeleteThreadResDto.class);
            Optional<AssistantThread> threadEntity = assistantThreadRepo.findThreadByThreadId(deleteThreadResDto.getId());
            threadEntity.ifPresent(thread -> assistantRepo.deleteById(thread.getId()));
        } catch (JsonProcessingException e) {
            log.warn("Json Processing Exception - deleteAssistant");
            throw new RuntimeException("Json Processing Exception - deleteAssistant");
        } catch (Exception e) {
            log.warn("Exception");
            throw new RuntimeException("Exception - deleteAssistant");
        }

    }


    public void syncSaveAssistant(CreateAssistantResDto dto) {
        Assistant entity = Assistant.builder()
                .assistantId(dto.getId())
                .isActive(true)
                .instructions(dto.getInstructions())
                .name(dto.getName())
                .version(dto.getName().split("_")[1])
                .build();
        try {
            assistantRepo.save(entity);
            // 익셉션 체크용
            // throw new RuntimeException("run time exception");
        } catch (RuntimeException e) {
            log.warn(e + "Save Assistant Error");
            log.warn(e + "Try Compensation Transaction");
            deleteAssistant(dto.getId());
            throw new RuntimeException("Save Assistant Error");
        }
    }

    public AssistantThread syncSaveThread(String threadId, String assistantId) {
        Optional<Assistant> assistant = assistantRepo.findAssistantByAssistantId(assistantId);

        // 어시스턴트가 어떠한 이유로 생성되지 않았거나 존재하지 않으면 DB에 이를 기록하지 않음.
        if (assistant.isEmpty()) {
            log.warn("NotFound Assistant - ID: " + assistantId);
            throw new RuntimeException("NotFound Assistant - ID: " + assistantId);
        }

        try {
            return assistantThreadRepo.save(AssistantThread.builder()
                    .assistant(assistant.get())
                    .threadId(threadId)
                    .isDeleteFromOpenAi(false)
                    .build());
        } catch (DataAccessException e) {
            log.error("Database access error when saving assistant thread for ID: " + assistantId, e);
            deleteThread(threadId);
            throw new RuntimeException("Database error occurred while saving assistant thread");
        } catch (Exception e) {
            log.error("Unexpected error occurred", e);
            deleteThread(threadId);
            throw new RuntimeException("Unexpected error occurred", e);
        }
        // TODO: 생성된 스레드 정보를 저장 ( thread_id )
    }

    public void saveMessage() {
        // TODO: 생성된 메시지 정보를 저장 ( 보낸 메시지, 스레드 아이디 등 )
    }
}
