package com.example.demo.ai.service;

import com.example.demo.ai.dto.assistant.CreateAssistantReqDto;
import com.example.demo.ai.dto.assistant.GetAssistantResDto;
import com.example.demo.ai.dto.assistant.GptApiCreateAssistantReqDto;
import com.example.demo.ai.dto.assistant.GptApiCreateAssistantResDto;
import com.example.demo.ai.dto.file.FileData;
import com.example.demo.ai.dto.file.FileDelete;
import com.example.demo.ai.dto.file.FileList;
import com.example.demo.ai.dto.message.CreateMessageDto;
import com.example.demo.ai.dto.message.CreateMessageResDto;
import com.example.demo.ai.dto.message.GetMessagesResDto;
import com.example.demo.ai.dto.run.CreateRunReqDto;
import com.example.demo.ai.dto.run.CreateRunResDto;
import com.example.demo.ai.dto.run.OneStepRunReqDto;
import com.example.demo.ai.dto.thread.CreateThreadResDto;
import com.example.demo.ai.dto.thread.DeleteThreadResDto;
import com.example.demo.ai.entity.Assistant;
import com.example.demo.ai.entity.AssistantThread;
import com.example.demo.ai.entity.AssistantThreadMessage;
import com.example.demo.ai.repo.AssistantRepo;
import com.example.demo.ai.repo.AssistantThreadMessageRepo;
import com.example.demo.ai.repo.AssistantThreadRepo;
import com.example.demo.ai.service.dto.DeleteAssistantResDto;
import com.example.demo.auth.AuthenticationFacade;
import com.example.demo.user.entity.User;
import com.example.demo.user.repo.UserRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.FileSystemResource;
import org.springframework.dao.DataAccessException;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import org.springframework.web.client.RestClient;

import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * <p>Chat GPT Assistans와 통신하기 위한 서비스 입니다.</p>
 */
@Slf4j
@Service
public class GptAssistantApiService {
    private final AssistantThreadMessageRepo assistantThreadMessageRepo;
    private final UserRepo userRepo;
    private final RestClient restClient;
    private final AssistantThreadRepo assistantThreadRepo;
    private final AssistantRepo assistantRepo;
    private final AuthenticationFacade authenticationFacade;
    private final ObjectMapper objectMapper;

    @Autowired
    public GptAssistantApiService(@Qualifier("gptAssistantRestClient") RestClient restClient,
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

    public FileData fileUploadAPI(MultipartFile multipartFile) {
        try {
            String uri = "/v1/files"; // Adjust to your endpoint

            // Convert MultipartFile to java.io.File
            java.io.File tempFile = java.io.File.createTempFile("upload-", multipartFile.getOriginalFilename());
            multipartFile.transferTo(tempFile);
            tempFile.deleteOnExit();

            // Prepare the file and headers
            FileSystemResource fileResource = new FileSystemResource(tempFile);
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", fileResource);
            body.add("purpose","assistants");

            ResponseEntity<FileData> response = restClient
                    .post()
                    .uri(uri)
                    .body(body)
                    .retrieve()
                    .toEntity(FileData.class);


            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println(response.getBody().getFileName());
                return response.getBody();
            } else {
                throw new RuntimeException("Failed to upload file: " + response.getStatusCode().toString());
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload file", e);
        }
    }

    /**
     * <p>파일을 리스트로 가져오는 메서드 입니다.</p>
     * */
    public Optional<FileList> getfiles(){
        String uri = "/v1/files";

        return restClient
                .get()
                .uri(uri)
                .exchange((request,response) -> {
                    if(response.getStatusCode().is4xxClientError()){
                        return Optional.empty();
                    }else{
                        try{
                            return Optional.of(objectMapper.readValue(response.getBody(), FileList.class));
                        }catch  (JsonProcessingException e){
                            log.warn("에러메시지: " + e.getMessage());
                            return Optional.empty();
                        }
                    }
                });

    }

    public Optional<FileDelete> deleteFile(String fileId){
        String uri = "/v1/files/" + fileId;

        return restClient.
                delete()
                .uri(uri)
                .exchange((request,response) -> {
                    if(response.getStatusCode().is4xxClientError()){
                        throw new RuntimeException("존재하지 않는 파일");
                    }else{
                        return Optional.of(objectMapper.readValue(response.getBody(),FileDelete.class));
                    }
                });
    }


    /**
     * <p>어시스턴트 생성 메서드 입니다.</p>
     */
    public GptApiCreateAssistantResDto createAssistantAPI(CreateAssistantReqDto dto) {
        if (!dto.getAssistantType().equals("fashion")) {
            throw new RuntimeException("생성 불가능한 타입 입니다.");
        }


        // TODO: 어시스턴트 생성시 DB에 해당 어시스턴트가 이미 있는지 조회해봄 버전, type으로 검색

        Optional<Assistant> assistant = assistantRepo.findAssistantByAssistantTypeAndVersion(dto.getAssistantType(), dto.getVersion());
        if (assistant.isPresent()) {
            throw new RuntimeException("이미 존재하는 어시스턴트 입니다.");
        }


        String uri = "/v1/assistants";
        ResponseEntity<String> json = restClient
                .post()
                .uri(uri)
                .body(GptApiCreateAssistantReqDto.builder()
                        .instructions(dto.getInstructions())
                        .name(dto.getName())
                        .model(dto.getModel())
                        .tools(dto.getTools())
                        .build())
                .retrieve()
                .toEntity(String.class);


        try {
            GptApiCreateAssistantResDto response = objectMapper.readValue(json.getBody(), GptApiCreateAssistantResDto.class);

            assistantRepo.save(Assistant.builder()
                    .instructions(response.getInstructions())
                    .name(response.getName())
                    .version(response.getName().split("_")[1])
                    .model(response.getModel())
                    .assistantType(dto.getAssistantType())
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
    public Optional<GetAssistantResDto.Data> getAssistantAPI(String assistantId) {
        String uri = "/v1/assistants/" + assistantId;
        return restClient
                .get()
                .uri(uri)
                .exchange((request, response) -> {
                    if (response.getStatusCode().is2xxSuccessful()) {
                        try {
                            GetAssistantResDto.Data dto = objectMapper.readValue(response.getBody(), GetAssistantResDto.Data.class);
                            return Optional.of(dto);
                        } catch (JsonProcessingException e) {
                            log.warn(e.getMessage());
                            return Optional.empty();
                        }
                    } else {
                        return Optional.empty();
                    }
                });
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
    public CreateThreadResDto createThreadAPI(String assistantId) {
//        userRepo.findByName();
        Long tempUser = 1L;
        User user = userRepo.findById(tempUser).orElseThrow(() -> new RuntimeException("유저가 존재하지 않습니다."));
        Assistant assistant = assistantRepo.findAssistantByAssistantId(assistantId).orElseThrow(() -> new RuntimeException("어시스턴트가 존재하지 않습니다."));
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
            CreateThreadResDto response = objectMapper.readValue(json.getBody(), CreateThreadResDto.class);
            assistantThreadRepo.save(AssistantThread.builder()
                    .user(user)
                    .assistant(assistant)
                    .threadId(response.getId())
                    .isDeleteFromOpenAi(false)
                    .build());
            return response;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JsonProcessingException");
        } catch (DataAccessException e) {
            throw new RuntimeException("DB Exception");
        } catch (Exception e) {
            throw new RuntimeException("Exception");
        }
    }


    /**
     * <p>스레드 단일 조회 메서드 입니다.</p>
     */
    public CreateThreadResDto getThread(String threadId) {
        String uri = "/v1/threads/" + threadId;
        ResponseEntity<String> json = restClient
                .get()
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
        AssistantThread assistantThread = assistantThreadRepo.findThreadByThreadId(threadId).orElseThrow(() -> new RuntimeException("스레드가 데이터베이스에 존재하지 않습니다."));

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
            CreateMessageResDto response = objectMapper.readValue(json.getBody(), CreateMessageResDto.class);
            if (response.getContent().stream().findFirst().isEmpty()) throw new RuntimeException("메시지 생성 에러");
            AssistantThreadMessage.builder()
                    .assistantThread(assistantThread)
                    .runId(null)
                    .role(response.getRole())
                    .value(response.getContent().stream().findFirst().get().getText().getValue())
                    .file_ids(null)
                    .annotataions(null)
                    .metadata(null)
                    .isDeleteFromOpenAi(false)
                    .build();
            return response;
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
    public CreateMessageResDto getMessageAPI(String threadId, String messageId) {
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
            return objectMapper.readValue(json.getBody(), CreateMessageResDto.class);
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
     *
     * @param threadId    실행 객체에 포함할 스레드 아이디 입니다. 해당 스레드 내에 있는 모든 메시지를 포함합니다.
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
     *
     * @param threadId 실행 객체에 포함된 스레드 아이디 입니다.
     * @param runId    실행객체의 아이디 입니다.
     */
    public CreateRunResDto getRun(String threadId, String runId) {
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


    /**
     * <p>스레드 생성과 메시지 추가 실행을 동시에 처리하는 메서드</p>
     *
     * @param assistantId 작업을 수행할 어시스턴트의 아이디
     * @param message     생성된 스레드에 포함할 단일 메시지
     * @param role        'user', 'assistant' 등 권한을 의미함. 기본 값은 user
     */
    public CreateRunResDto oneStepRun(String role, String message, String assistantId) {
        if (!role.equals("user") && !role.equals("assistant")) {
            role = "user";
        }
        List<CreateMessageDto> messages = new ArrayList<>();
        messages.add(CreateMessageDto.builder().role(role).content(message).build());

        OneStepRunReqDto.Thread reqThread = OneStepRunReqDto.Thread.builder().messages(messages).build();
        OneStepRunReqDto oneStepRunReqDto = OneStepRunReqDto.builder().assistantId(assistantId).thread(reqThread).build();

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
            CreateRunResDto response = objectMapper.readValue(json.getBody(), CreateRunResDto.class);

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


    // TODO: oneStepRun 메서드 실행시 스레드, 메시지 정보 DB에 기록
    // TODO: 어시스턴트, 스레드 조회시 없는 항목이라면 is_delete true로 변경
    // TODO: DB에 저장된 어시스턴트 히스토리, 스레드 히스토리 조회시 is_delete false 인 항목만 조회하도록 구현
}
