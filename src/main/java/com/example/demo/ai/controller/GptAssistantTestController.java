package com.example.demo.ai.controller;

import com.example.demo.ai.dto.CreateAssistantReqDto;
import com.example.demo.ai.dto.CreateAssistantResDto;
import com.example.demo.ai.dto.GetAssistantResDto;
import com.example.demo.ai.service.GptAssistantService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor

public class GptAssistantTestController {
    private final GptAssistantService gptAssistantService;

    /**
     * <p>어시스턴트 목록 조회</p>
     * */
    @GetMapping("/ai/assistants")
    public GetAssistantResDto getAssistants() {
        return gptAssistantService.getAssistants();
    }

    /**
     * 어시스턴트 생성
     * */
    @PostMapping("/ai/assistants")
    public CreateAssistantResDto createAssistant(
            @RequestBody
            CreateAssistantReqDto reqDto
    ){
        String INSTRUCTIONS = "You are a fashion expert who knows well about temperature and clothes. Try to recommend people the right fashion for each temperature";
        String NAME = "Fashion Expert";
        String TARGET_MODEL = "gpt-3.5-turbo";

        System.out.println(reqDto.getName() + " : name");
        return gptAssistantService.createAssistant(INSTRUCTIONS,NAME,TARGET_MODEL);
    }
}
