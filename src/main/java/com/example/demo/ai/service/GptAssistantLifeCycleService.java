package com.example.demo.ai.service;

import com.example.demo.ai.dto.assistant.CreateAssistantResDto;
import com.example.demo.ai.entity.Assistant;
import com.example.demo.ai.repo.AssistantRepo;
import com.example.demo.ai.service.dto.DeleteAssistantResDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import javax.swing.text.html.Option;
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
    private final ObjectMapper objectMapper;

    @Qualifier("gptAssistantRestClient")
    private final RestClient restClient;

    GptAssistantLifeCycleService(
            @Qualifier("gptAssistantRestClient") RestClient restClient,
            ObjectMapper objectMapper,
            AssistantRepo assistantRepo) {
        this.assistantRepo = assistantRepo;
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
            Optional<Assistant> assistantEntity = assistantRepo.findAssistantByName(deleteAssistantResDto.getId());

            assistantEntity.ifPresent(assistant -> assistantRepo.deleteById(assistant.getId()));

        } catch (JsonProcessingException e) {
            log.warn("Json Processing Exception - deleteAssistant");
            throw new RuntimeException("Json Processing Exception - deleteAssistant");
        } catch (RuntimeException e) {
            log.warn("Runtime Exception");
            throw new RuntimeException("Json Processing Exception - deleteAssistant");
        }
    }


    public void syncSaveAssistant(CreateAssistantResDto dto) {
        Assistant entity = Assistant.builder()
                .assistantId(dto.getId())
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

    public void saveThread() {
        // TODO: 생성된 스레드 정보를 저장 ( thread_id )
    }

    public void saveMessage() {
        // TODO: 생성된 메시지 정보를 저장 ( 보낸 메시지, 스레드 아이디 등 )
    }
}
