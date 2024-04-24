package com.example.demo.ai.controller;


import com.example.demo.ai.dto.assistant.CreateAssistantReqDto;
import com.example.demo.ai.dto.assistant.GetAssistantResDto;
import com.example.demo.ai.dto.assistant.GptApiCreateAssistantResDto;
import com.example.demo.ai.dto.file.FileData;
import com.example.demo.ai.dto.file.FileDelete;
import com.example.demo.ai.dto.file.FileList;
import com.example.demo.ai.dto.messages.v2.messages.Message;
import com.example.demo.ai.dto.messages.CreateMessageResDto;
import com.example.demo.ai.dto.messages.GetMessagesResDto;
import com.example.demo.ai.dto.run.RunCreateRequest;
import com.example.demo.ai.dto.run.v2.Run;
import com.example.demo.ai.dto.run.CreateThreadAndRunRequest;
import com.example.demo.ai.dto.thread.CreateThreadResDto;
import com.example.demo.ai.dto.thread.DeleteThreadResDto;
import com.example.demo.ai.service.GptAssistantApiService;

import com.example.demo.ai.service.GptService;
import com.example.demo.ai.service.dto.DailyCodyResDto;
import com.example.demo.ai.service.dto.DeleteAssistantResDto;
import com.example.demo.weather.dto.fcst.FcstItem;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor

public class GptAssistantTestController {
    private final GptAssistantApiService gptAssistantApiService;
    private final GptService gptService;


    @PostMapping("/v1/assistants")
    public GptApiCreateAssistantResDto createAssistant(
            @RequestBody
            CreateAssistantReqDto dto
    ) {

        dto.getTools().forEach(item -> System.out.println(item.getType() + "type"));

        return gptAssistantApiService.createAssistantAPI(dto);
    }

    @GetMapping("/v1/assistants/{assistantId}")
    public Optional<GetAssistantResDto.Data> getAssistant(
            @PathVariable("assistantId")
            String assistantId
    ) {
        return gptAssistantApiService.getAssistantAPI(assistantId);
    }

    @GetMapping("/v1/assistants")
    public GetAssistantResDto getAssistants() {
        return gptAssistantApiService.getAssistantsAPI();
    }

    @DeleteMapping("/v1/assistants/{assistantId}")
    public DeleteAssistantResDto deleteAssistants(@PathVariable("assistantId") String assistantId) {
        return gptAssistantApiService.deleteAssistantAPI(assistantId);
    }

    @GetMapping("/v1/threads/{threadId}")
    public CreateThreadResDto getThread(@PathVariable("threadId") String threadId) {
        return gptAssistantApiService.getThread(threadId);
    }

    @PostMapping("/v1/threads")
    public CreateThreadResDto createThread(
            @RequestBody
            RunCreateRequest dto
    ) {
        return gptAssistantApiService.createThreadAPI(dto.getAssistantId());
    }

    @DeleteMapping("/v1/threads/{threadId}")
    public DeleteThreadResDto deleteThreadResDto(@PathVariable("threadId") String threadId) {
        return gptAssistantApiService.deleteThreadAPI(threadId);
    }

    @PostMapping("/v1/threads/{threadId}/messages")
    public CreateMessageResDto createMessage(
            @PathVariable("threadId")
            String threadId,
            @RequestBody
            Message dto
    ) {
        return gptAssistantApiService.createMessageAPI(dto.getRole(), dto.getContent(), threadId);
    }

    @GetMapping("/v1/threads/{threadId}/messages/{messageId}")
    public CreateMessageResDto getMessage(
            @PathVariable("threadId")
            String threadId,
            @PathVariable("messageId")
            String messageId
    ) {
        return gptAssistantApiService.getMessageAPI(threadId, messageId);
    }

    @GetMapping("/v1/threads/{threadId}/messages")
    public GetMessagesResDto getMessages(
            @PathVariable("threadId")
            String threadId
    ) {
        return gptAssistantApiService.getMessagesAPI(threadId);
    }

    @PostMapping("/v1/threads/{threadId}/runs")
    public Run createRun(
            @PathVariable("threadId")
            String threadId,
            @RequestBody
            RunCreateRequest dto
    ) {
        return gptAssistantApiService.run(threadId, dto.getAssistantId());
    }

    @GetMapping("/v1/threads/{threadId}/runs/{runId}")
    public Run getRun(
            @PathVariable("threadId")
            String threadId,
            @PathVariable("runId")
            String runId
    ) {
        return gptAssistantApiService.getRun(threadId, runId);
    }




    @PostMapping("/v1/files")
    public FileData uploadFile(
            @RequestParam("uploadFile")
            MultipartFile uploadFile,
            HttpServletRequest req

    ) {
        return gptAssistantApiService.fileUploadAPI(uploadFile);
    }

    @GetMapping("/v1/files")
    public Optional<FileList> getFiles() {
        return gptAssistantApiService.getfiles();
    }

    @DeleteMapping("/v1/files/{fileId}")
    public Optional<FileDelete> deleteFile(
            @PathVariable("fileId")
            String fileId
    ) {
        return gptAssistantApiService.deleteFile(fileId);
    }
}

/**
 * thread_8EZ9Fm2oxQI0iBuEiK9SX1CG
 * thread_R8aj36ScCZLKvK4drIYtA391
 * thread_LO2j8zzCt0GlGiSxhIe18DGo
 * thread_BnhYznQAq8HoQclYjTpABCDN
 * thread_hxgakXQdvkcb9PS5WVkZE5xN
 */


