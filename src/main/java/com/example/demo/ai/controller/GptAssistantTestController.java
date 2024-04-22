package com.example.demo.ai.controller;


import com.example.demo.ai.dto.assistant.CreateAssistantReqDto;
import com.example.demo.ai.dto.assistant.CreateAssistantResDto;
import com.example.demo.ai.dto.assistant.GetAssistantResDto;
import com.example.demo.ai.service.GptAssistantApiService;

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
    ){
        return gptAssistantApiService.createAssistantAPI(dto);
    }

    @GetMapping("/v1/assistants/{assistantId}")
    public GetAssistantResDto.Data getAssistant(
            @PathVariable("assistantId")
            String assistantId
    ){
        return gptAssistantApiService.getAssistantAPI(assistantId);
    }

    @GetMapping("/v1/assistants")
    public GetAssistantResDto getAssistants(){
        return gptAssistantApiService.getAssistantsAPI();
    }

}
