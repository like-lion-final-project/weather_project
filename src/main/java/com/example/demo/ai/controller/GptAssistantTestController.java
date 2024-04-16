package com.example.demo.ai.controller;

import com.example.demo.ai.dto.GetAssistantDto;
import com.example.demo.ai.service.GptAssistantService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor

public class GptAssistantTestController {
    private final GptAssistantService gptAssistantService;

    /**
     * <p>어시스턴트 목록 조회</p>
     * */
    @GetMapping("/ai/assistants")
    public GetAssistantDto getAssistants() {
        return gptAssistantService.getAssistants();
    }
}
