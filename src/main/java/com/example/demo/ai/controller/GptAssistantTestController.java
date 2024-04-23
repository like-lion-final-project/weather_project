package com.example.demo.ai.controller;


import com.example.demo.ai.dto.assistant.CreateAssistantReqDto;
import com.example.demo.ai.dto.assistant.CreateAssistantResDto;
import com.example.demo.ai.dto.assistant.GetAssistantResDto;
import com.example.demo.ai.dto.message.CreateMessageDto;
import com.example.demo.ai.dto.message.CreateMessageResDto;
import com.example.demo.ai.dto.message.GetMessagesResDto;
import com.example.demo.ai.dto.run.CreateRunReqDto;
import com.example.demo.ai.dto.run.CreateRunResDto;
import com.example.demo.ai.dto.run.OneStepRunParamDto;
import com.example.demo.ai.dto.run.OneStepRunReqDto;
import com.example.demo.ai.dto.thread.CreateThreadReqDto;
import com.example.demo.ai.dto.thread.CreateThreadResDto;
import com.example.demo.ai.dto.thread.DeleteThreadResDto;
import com.example.demo.ai.service.GptAssistantApiService;

import com.example.demo.ai.service.dto.DeleteAssistantResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor

public class GptAssistantTestController {
    private final GptAssistantApiService gptAssistantApiService;


    @PostMapping("/v1/assistants")
    public CreateAssistantResDto createAssistant(
            @RequestBody
            CreateAssistantReqDto dto
    ) {
        return gptAssistantApiService.createAssistantAPI(dto);
    }

    @GetMapping("/v1/assistants/{assistantId}")
    public GetAssistantResDto.Data getAssistant(
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
    public CreateThreadResDto getThread(@PathVariable("threadId") String threadId){
        return gptAssistantApiService.getThread(threadId);
    }

    @PostMapping("/v1/threads")
    public CreateThreadResDto createThread(
            @RequestBody
            CreateRunReqDto dto
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
            CreateMessageDto dto
    ) {
        return gptAssistantApiService.createMessageAPI(dto.getRole(), dto.getContent(), threadId);
    }

    @GetMapping("/v1/threads/{threadId}/messages/{messageId}")
    public GetMessagesResDto getMessage(
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
    public CreateRunResDto createRun(
            @PathVariable("threadId")
            String threadId,
            @RequestBody
            CreateRunReqDto dto
    ){
        return gptAssistantApiService.run(threadId,dto.getAssistantId());
    }

    @GetMapping("/v1/threads/{threadId}/runs/{runId}")
    public CreateRunResDto getRun(
            @PathVariable("threadId")
            String threadId,
            @PathVariable("runId")
            String runId
    ){
        return gptAssistantApiService.getRun(threadId,runId);
    }

    @PostMapping("/v1/threads/runs")
    public CreateRunResDto onStepRun(
            @RequestBody
            OneStepRunParamDto dto

    ){
        return gptAssistantApiService.oneStepRun(dto.getRole(),dto.getMessage(),dto.getAssistantId());
    }
}

/**
 * thread_8EZ9Fm2oxQI0iBuEiK9SX1CG
 * thread_R8aj36ScCZLKvK4drIYtA391
 * thread_LO2j8zzCt0GlGiSxhIe18DGo
 * thread_BnhYznQAq8HoQclYjTpABCDN
 */
