package com.example.demo.ai.controller;

import com.example.demo.ai.AppConstants;
import com.example.demo.ai.dto.CreateAssistantResDto;
import com.example.demo.ai.dto.GetAssistantResDto;
import com.example.demo.ai.service.GptAssistantService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
    ){
        return gptAssistantService.createAssistant(AppConstants.INSTRUCTIONS , AppConstants.NAME, AppConstants.MODEL);
    }

    /** 스레드 생성 */
    @PostMapping("/ai/threads")
    public void createThread(
            @RequestParam("user_id")
            Integer userId
    ){
        gptAssistantService.createThread(userId);
    }

    /**메시지 생성*/
    @PostMapping("/ai/threads")
    public void createMessage(
            @RequestParam("c")
            String c,
            @RequestParam("a")
            String age,
            @RequestParam("g")
            String gender
    ){

    }
}
