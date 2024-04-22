package com.example.demo.ai.service;

import com.example.demo.ai.AppConstants;
import com.example.demo.ai.dto.GetModelsResDto;
import com.example.demo.ai.dto.SyncDto;
import com.example.demo.ai.dto.assistant.CreateAssistantReqDto;
import com.example.demo.ai.dto.assistant.CreateAssistantResDto;
import com.example.demo.ai.dto.assistant.GetAssistantResDto;
import com.example.demo.ai.dto.message.CreateMessageDto;
import com.example.demo.ai.dto.message.CreateMessageResDto;
import com.example.demo.ai.dto.thread.CreateThreadResDto;
import com.example.demo.ai.dto.thread.DeleteThreadResDto;
import com.example.demo.ai.entity.Assistant;
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
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>GPT Assistant api의 결과를 저장하고 라이프 사이클을 관리하는 서비스 입니다.</p>
 * <p>다른 패키지의 서비스에서 마음대로 사용할 수 없도록 패키지로 접근 제한</p>
 */
@Slf4j
@Service
class GptAssistantCoreService {
    private final UserRepo userRepo;
    private final AssistantRepo assistantRepo;
    private final AssistantThreadRepo assistantThreadRepo;
    private final AssistantThreadMessageRepo assistantThreadMessageRepo;
    private final ObjectMapper objectMapper;

    @Qualifier("gptAssistantRestClient")
    private final RestClient restClient;

    GptAssistantCoreService(
            @Qualifier("gptAssistantRestClient") RestClient restClient,
            ObjectMapper objectMapper,
            UserRepo userRepo,
            AssistantRepo assistantRepo,
            AssistantThreadRepo assistantThreadRepo,
            AssistantThreadMessageRepo assistantThreadMessageRepo
    ) {
        this.userRepo = userRepo;
        this.assistantRepo = assistantRepo;
        this.assistantThreadRepo = assistantThreadRepo;
        this.restClient = restClient;
        this.objectMapper = objectMapper;
        this.assistantThreadMessageRepo = assistantThreadMessageRepo;
    }

    /**
     * <p>현재 Chat GPT API 에서 사용 가능한 모델 정보를 반환하는 메서드</p>
     * <p>DEFAULT_MODEL_IDENTIFIER_LIST 변수에 저장된 기본 값들과 응답 값을 비교해서 교집합 데이터만 반환함</p>
     */

    public Set<String> getActiveModels() {
        String url = "/v1/models";
        String jsonResponse = restClient
                .get()
                .uri(url)
                .retrieve()
                .body(String.class);

        try {
            GetModelsResDto models = objectMapper.readValue(jsonResponse, GetModelsResDto.class);
            Set<String> activeModels = new HashSet<>();
            for (GetModelsResDto.Data model : models.getData()) {
                activeModels.add(model.getId());
            }

            activeModels.retainAll(AppConstants.DEFAULT_MODEL_IDENTIFIER_LIST);
            return activeModels;
        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage() + "JSON parsing error");
            throw new RuntimeException("JSON parsing error", e);
        }
    }


    /**
     * <p>타겟 모델이 사용가능한 상태인지 체크하는 메서드</p>
     *
     * @param modelName 모델의 이름입니다. ex) gpt-3.5-turbor-0613
     */

    public boolean isActiveTargetModel(String modelName) {
        Set<String> activeModels = getActiveModels(); // Use the getActiveModels method
        return activeModels.contains(modelName); // Check if the model is in the active models set
    }


    /**
     * <p>어시스턴트 목록을 가져오는 API 입니다.</p>
     *
     * @return 어시스턴트 목록
     */
    public GetAssistantResDto getAssistantsAPI() {
        String url = "v1/assistants";

        String jsonResponse = restClient
                .get()
                .uri(url)
                .retrieve()
                .body(String.class);

        try {
            return objectMapper.readValue(jsonResponse, GetAssistantResDto.class);
        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage() + "에러 메시지");
            throw new RuntimeException("json 가공 에러");
        }
    }


    /**
     * <p>어시스턴트 정보를 OpenAI에 저장하는 API 입니다.</p>
     *
     * @return 생성된 어시스턴트 정보 혹은 null
     */

    private CreateAssistantResDto createAssistantAPI(String instructions, String name, String model) {
        String url = "/v1/assistants";
        CreateAssistantReqDto dto = CreateAssistantReqDto.builder()
                .instructions(instructions)
                .name(name)
                .model(model)
                .build();

        ResponseEntity<String> jsonResponse = restClient.post()
                .uri(url)
                .body(dto)
                .retrieve()
                .toEntity(String.class);

        if (!jsonResponse.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Failed to create assistant - Open AI API: " + jsonResponse.getStatusCode());
        }

        try {
            return objectMapper.readValue(jsonResponse.getBody(), CreateAssistantResDto.class);
        } catch (JsonProcessingException e) {
            log.error("JSON Serialization Error: " + e.getMessage());
            throw new RuntimeException("JSON Serialization Exception - createAssistant");
        } catch (Exception e) {
            log.error("Failed to create assistant - Exception occurred during the process: " + e.getMessage());
            throw new RuntimeException("Exception during assistant creation: " + e.getMessage());
        }
    }


    /**
     * <p>스레드 정보를 가져오는 메서드 입니다.</p>
     *
     * @return 인자로 받은 스레드 정보 혹은 존재하지 않음 응답
     */

    public CreateThreadResDto getThreadAPI(String threadId) {
        String url = "v1/threads/" + threadId;

        ResponseEntity<String> jsonResponse = restClient
                .get()
                .uri(url)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                    return;
                })
                .toEntity(String.class);


        try {
            return objectMapper.readValue(jsonResponse.getBody(), CreateThreadResDto.class);
        } catch (JsonProcessingException e) {
            log.warn("Json Processing Exception");
            throw new RuntimeException("Json Processing Exception");
        }
    }


    /**
     * <p>어시스턴트 정보를 DB에 저장하는 메서드 입니다.</p>
     *
     * @return 어시스턴트 엔티티 정보 혹은 null
     */

    public Assistant createAssistantDB(String instructions, String name, String model, String version, boolean isActive) {
        Optional<Assistant> assistant = assistantRepo.findAssistantByName(name);
        if (assistant.isEmpty()) {
            return assistantRepo.save(
                    Assistant.builder()
                            .name(name)
                            .isActive(isActive)
                            .version(version)
                            .isDelete(false)
                            .instructions(instructions)
                            .model(model)
                            .build()
            );
        }
        return null;
    }


    /**
     * <p>어시스턴트 아이디를 DB에 업데이트 하는 메서드 입니다.</p>
     * <p>DB에 어시스턴트 정보 생성 하고 OpenAI에 어시스턴트를 생성한 뒤 생성된 정보를 바탕으로 해당하는 row를 업데이트 할 때 사용합니다.</p>
     */
    private void updateAssistantIdDB(Assistant assistant, String newAssistantId) {
        System.out.println(assistant.getId() + "IDID");
        Optional<Assistant> assistantEntity = assistantRepo.findAssistantByName(assistant.getName());

        System.out.println(assistant.getName() + "GetNAme");
        System.out.println(assistantEntity.isPresent() + "IsPresent");
        if (assistantEntity.isPresent()) {

            assistantRepo.save(Assistant.builder()
                    .id(assistant.getId())
                    .assistantId(newAssistantId)
                    .instructions(assistant.getInstructions())
                    .name(assistant.getName())
                    .version(assistant.getVersion())
                    .model(assistant.getModel())
                    .isActive(assistant.getIsActive())
                    .isDelete(assistant.getIsDelete())
                    .build());
        }

    }


    /**
     * <p>스레드 생성 메서드 입니다.</p>
     */

    public AssistantThread createThreadDB(Long userId, String assistantId) {
        // TODO: DB에 스레드 저장 이후 동기화 관련은 해당 메서드에서 신경쓰지 않음
        Optional<User> userEntity = userRepo.findById(userId);
        Optional<Assistant> assistant = assistantRepo.findAssistantByAssistantId(assistantId);
        if (userEntity.isEmpty()) {
            throw new RuntimeException("존재하지 않는 유저 입니다.");
        } else if (assistant.isEmpty()) {
            throw new RuntimeException("존재하지 않는 어시스턴트 입니다.");
        }

        return assistantThreadRepo.save(
                AssistantThread.builder()
                        .user(userEntity.get())
                        .createdAt(LocalDateTime.now())
                        .assistant(assistant.get())
                        .isDeleteFromOpenAi(false)
                        .build());
    }


    /**
     * <p>스레드 아이디를 업데이트 하는 메서드 입니다. API로 생성후 생성된 threadId값을 저장함</p>
     */

    private void updateThreadIdDB(AssistantThread assistantThread, String newThreadId) {
        System.out.println(assistantThread.getId() + "아이디");
        System.out.println(newThreadId + "새로운 스레드 아이디");
        Optional<AssistantThread> assistantThreadEntity = assistantThreadRepo.findFirstByUserIdAndIsDeleteFromOpenAiFalseOrderByCreatedAtDesc(assistantThread.getUser().getId());
        assistantThreadEntity.ifPresent(thread -> assistantThreadRepo.save(
                AssistantThread.builder()
                        .id(thread.getId())
                        .user(assistantThread.getUser())
                        .assistant(assistantThread.getAssistant())
                        .threadId(newThreadId)
                        .isDeleteFromOpenAi(false)
                        .build()
        ));
    }


    /**
     * <p>DB에 저장된 스레드를 삭제하는 메서드 입니다.</p>
     * <p>값을 실제로 삭제 하는게 아닌 논리적으로 삭제하는 메서드 입니다.</p>
     */

    private AssistantThread deleteThreadDB(String threadId) {
        Optional<AssistantThread> assistantThreadOptional = assistantThreadRepo.findThreadByThreadId(threadId);
        return assistantThreadOptional.map(thread -> {
            AssistantThread updatedThread = AssistantThread.builder()
                    .id(thread.getId())
                    .user(thread.getUser())
                    .assistant(thread.getAssistant())
                    .threadId(thread.getThreadId())
                    .isDeleteFromOpenAi(true)
                    .build();
            return assistantThreadRepo.save(updatedThread);
        }).orElse(null);
    }


    /**
     * <p>스레드 생성 API 입니다</p>
     *
     * @param userId User 엔티티에서 유저를 식별할 고유 값을 의미합니다. 현재 임시 값이며 User 엔티티 구조에 따라 변경 될 수 있습니다.
     */

    @Transactional
    public CreateThreadResDto createThreadAPI(Long userId, String assistantId) {
        String url = "/v1/threads";
        Optional<AssistantThread> existingThread = assistantThreadRepo.findFirstByUserIdAndIsDeleteFromOpenAiFalseOrderByCreatedAtDesc(userId);

        if (existingThread.isPresent() && existingThread.get().getThreadId() != null) {
            return null;
        }

        User userEntity = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("존재하지 않는 유저"));
        Assistant assistant = assistantRepo.findAssistantByAssistantId(assistantId).orElseThrow(
                () -> new RuntimeException("어시스턴트를 찾을 수 없습니다.")
        );

        ResponseEntity<String> jsonResponse = restClient
                .post()
                .uri(url)
                .retrieve()
                .toEntity(String.class);

        if (jsonResponse.getStatusCode().is4xxClientError()) {
            System.out.println("400번대 에러");
            return null;
        }

        String threadId = null;
        try {
            CreateThreadResDto createThreadResDto = objectMapper.readValue(jsonResponse.getBody(), CreateThreadResDto.class);
            threadId = createThreadResDto.getId();
            return createThreadResDto;
        } catch (JsonProcessingException e) {
            log.warn("----  Json Processing Exception  ----");
            log.info(jsonResponse + " : JsonResponse");
            log.warn("-------------------------------------");
            deleteThreadAPI(threadId);
            deleteThreadDB(threadId);
            throw new RuntimeException("Json Processing Exception");
        } catch (Exception e) {
            deleteThreadAPI(threadId);
            deleteThreadDB(threadId);
            throw new RuntimeException("Exception occurred during thread creation", e);
        }
    }


    /**
     * <p>스레드 삭제 API 입니다.</p>
     */
    private void deleteThreadAPI(String threadId) {
        String url = "/v1/threads/" + threadId;

        try {
            String jsonResponse = restClient
                    .delete()
                    .uri(url)
                    .retrieve()
                    .body(String.class);

            DeleteThreadResDto deleteThreadResDto = objectMapper.readValue(jsonResponse, DeleteThreadResDto.class);
            Optional<AssistantThread> threadEntity = assistantThreadRepo.findThreadByThreadId(deleteThreadResDto.getId());
            threadEntity.ifPresent(thread -> assistantThreadRepo.deleteById(thread.getId()));
        } catch (JsonProcessingException e) {
            log.warn("Json Processing Exception - deleteAssistant");
            throw new RuntimeException("Json Processing Exception - deleteAssistant");
        } catch (Exception e) {
            log.warn("Exception");
            throw new RuntimeException("Exception - deleteAssistant");
        }

    }


    /**
     * <p>어시스턴트 삭제 메서드 입니다. 같은 패키지 내에서도 사용할 수 없도록 private 접근 제한자 설정</p>
     */
    private void deleteAssistantAPI(String assistantId) {
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


    /**
     * <p>메시지 생성 API</p>
     */

    public CreateMessageResDto createMessageAPI(String threadId, String message) {
        // TODO: 스레드에 추가할 메시지 생성

        String url = "/v1/threads/" + threadId + "/messages";

        CreateMessageDto messageReqDto = CreateMessageDto.builder()
                .role("user")
                .content(message)
                .build();

        ResponseEntity<String> jsonResponse = restClient
                .post()
                .uri(url)
                .body(messageReqDto)
                .retrieve()
                .toEntity(String.class);

        try {
            return objectMapper.readValue(jsonResponse.getBody(), CreateMessageResDto.class);
        } catch (JsonProcessingException e) {
            log.info("JSON 직렬화 에러");
            return null;
        }
    }


    /**
     * <p>메시지 생성 메서드</p>
     */

    public AssistantThreadMessage createMessageDB(String message) {
        // TODO: 스레드에 추가할 메시지 생성
        Long tempUser = 1L;
        User userEntity = userRepo.findById(tempUser).orElseThrow(
                () -> new RuntimeException("존재하지 않는 유저")
        );

        AssistantThread assistantThread = assistantThreadRepo.findFirstByUserIdAndIsDeleteFromOpenAiFalseOrderByCreatedAtDesc(userEntity.getId())
                .orElseThrow(() -> new RuntimeException("해당 유저의 스레드가 존재하지 않습니다"));

        return assistantThreadMessageRepo.save(
                AssistantThreadMessage.builder()
                        .assistantThread(assistantThread)
                        .type("text")
                        .value(message)
                        .build());
    }


    /**
     * <p>어시스턴트 동기화 메서드 입니다.</p>
     * <p>OpenAI에 저장된 정보와 DB에 저장된 정보를 동기화 합니다. 서비스 DB가 메인이고, DB에 없는 항목은 삭제합니다.</p>
     * <pre>
     *     <b>OpenAI</b>에 저장된 어시스턴트 -> DB에 해당 정보가 없다면 삭제
     *     <b>DB</b>에 저장된 어시스턴트 정보 -> OpenAI에 없다면 신규생성
     *     <b>둘다 없다면</b> -> DB, OpenAI 모두 생성
     * </pre>
     */

    @Transactional
    public void synchronizeAssistants() {
        List<GetAssistantResDto.Data> apiAssistants = getAssistantsAPI().getData();
        List<Assistant> dbAssistants = assistantRepo.findAll();

        Map<String, Assistant> dbAssistantMap = dbAssistants.stream()
                .collect(Collectors.toMap(Assistant::getAssistantId, Function.identity(), (existing, replacement) -> existing));

        SyncDto.SyncDtoBuilder syncResult = SyncDto.builder().syncCreated(false).syncDeleted(false);

        if (apiAssistants.isEmpty() && dbAssistants.isEmpty()) {
            log.info("No assistants found in both OpenAI and DB.");
            syncResult.build();
            return;
        }

        for (GetAssistantResDto.Data apiAssistant : apiAssistants) {
            if (!dbAssistantMap.containsKey(apiAssistant.getId())) {
                deleteAssistantAPI(apiAssistant.getId());
                log.info("Deleted orphaned assistant from OpenAI: " + apiAssistant.getName());
                syncResult.syncDeleted(true);
            }
        }

        for (Assistant dbAssistant : dbAssistants) {
            if (apiAssistants.stream().noneMatch(apiAssistant -> apiAssistant.getId().equals(dbAssistant.getAssistantId()))) {
                String name = dbAssistant.getName();
                CreateAssistantResDto createdAssistant = createAssistantAPI(dbAssistant.getInstructions(), name, dbAssistant.getModel());
                updateAssistantIdDB(dbAssistant, createdAssistant.getId());
                log.info("Created missing assistant in OpenAI: " + dbAssistant.getName());
                syncResult.syncCreated(true);
            }
        }
    }


    /**
     * <p>스레드 동기화 메서드 입니다. 각 유저들의 스레드를 조회하고 동기화 합니다.</p>
     */

    @Transactional
    public void synchronizeThread() {
        Long tempUserId = 1L;
        Optional<AssistantThread> dbAssistantThread = assistantThreadRepo.findFirstByUserIdAndIsDeleteFromOpenAiFalseOrderByCreatedAtDesc(tempUserId);

        // DB에 스레드 정보가 없으면 아무것도 수행하지 않음. 이전 메서드가 제대로 작동하지 않은 것이라 판단함
        if (dbAssistantThread.isEmpty()) {
            return;
        }

        // DB에 기록된 스레드 정보에 스레드 아이디가 없다면 OpenAI에 신규 생성하고 그 값을 업데이트
        if (dbAssistantThread.get().getThreadId() == null) {
            CreateThreadResDto createThreadResDto = createThreadAPI(tempUserId, dbAssistantThread.get().getAssistant().getAssistantId());
            if (createThreadResDto == null) return;
            updateThreadIdDB(dbAssistantThread.get(), createThreadResDto.getId());
            return;
        }

        // DB에 스레드 정보가 기록되어있지만 API로 조회했을 땐 존재하지 않을 때, 신규생성함
        if (getThreadAPI(dbAssistantThread.get().getThreadId()) == null) {
            CreateThreadResDto createThreadResDto = createThreadAPI(tempUserId, dbAssistantThread.get().getAssistant().getAssistantId());
            if (createThreadResDto == null) return;
            updateThreadIdDB(dbAssistantThread.get(), createThreadResDto.getId());
        }

        // 신규 생성시 기존에 존재하던 항목을 논리적 삭제 하는 로직이 필요함
    }

}
