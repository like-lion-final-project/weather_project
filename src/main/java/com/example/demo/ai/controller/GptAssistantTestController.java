package com.example.demo.ai.controller;


import com.example.demo.ai.dto.assistant.CreateAssistantReqDto;
import com.example.demo.ai.dto.assistant.CreateAssistantResDto;
import com.example.demo.ai.dto.assistant.GetAssistantResDto;
import com.example.demo.ai.dto.thread.CreateThreadResDto;
import com.example.demo.ai.dto.thread.DeleteThreadResDto;
import com.example.demo.ai.service.GptAssistantApiService;

import com.example.demo.ai.service.dto.DeleteAssistantResDto;
import lombok.RequiredArgsConstructor;
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

    @PostMapping("/v1/threads")
    public CreateThreadResDto createThread() {
        return gptAssistantApiService.createThreadAPI();
    }

    @DeleteMapping("/v1/threads/{threadId}")
    public DeleteThreadResDto deleteThreadResDto(@PathVariable("threadId") String threadId) {
        return gptAssistantApiService.deleteThreadAPI(threadId);
    }

}
