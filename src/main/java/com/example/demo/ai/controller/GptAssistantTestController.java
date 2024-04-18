package com.example.demo.ai.controller;

import com.example.demo.ai.AppConstants;
import com.example.demo.ai.dto.assistant.CreateAssistantResDto;
import com.example.demo.ai.dto.assistant.GetAssistantResDto;
import com.example.demo.ai.dto.message.*;
import com.example.demo.ai.dto.run.CreateRunResDto;
import com.example.demo.ai.entity.AssistantThread;
import com.example.demo.ai.service.GptAssistantService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor

public class GptAssistantTestController {
    private final GptAssistantService gptAssistantService;

    /**
     * <p>어시스턴트 목록 조회</p>
     */
    @GetMapping("/ai/assistants")
    public GetAssistantResDto getAssistants() {
        return gptAssistantService.getAssistants();
    }

    /**
     * 어시스턴트 생성
     */
    @PostMapping("/ai/assistants")
    public CreateAssistantResDto createAssistant(
    ) {
        return gptAssistantService.createAssistant(AppConstants.INSTRUCTIONS, AppConstants.NAME + "_" + AppConstants.VERSION, AppConstants.MODEL);
    }

    /**
     * 스레드 생성
     */
    @PostMapping("/ai/threads")
    public AssistantThread createThread(
            @RequestParam("user_id")
            Integer userId
    ) {
        return gptAssistantService.createThread(userId);
    }

    @PostMapping("/ai/create-and-run")
    public CreateThreadAndRunResDto createThreadAndRun(
            @RequestParam("assistant_id")
            String assistantId,
            @RequestParam("c")
            String c,
            @RequestParam("a")
            String age,
            @RequestParam("g")
            String gender
            ) {
        String message = "C:" + c + ",A:" + age + ",G:" + gender + " Please keep the response data format.";
        List<CreateMessageDto> messages =  new ArrayList<>();

        messages.add(CreateMessageDto.builder()
                .role("user")
                .content(message)
                .build());

        CreateThreadAndRunReqDto.Thread.builder()
                .messages(messages)
                .build();
        return gptAssistantService.createThreadAndRun(assistantId,messages);
    }

    /**
     * 메시지 생성
     */
    @PostMapping("/ai/threads/{threadId}/messages")
    public CreateMessageResDto createMessage(
            @RequestParam("c")
            String c,
            @RequestParam("a")
            String age,
            @RequestParam("g")
            String gender,
            @PathVariable("threadId")
            String threadId
    ) {
        // 메시지 포메팅
//        String message = "C:25,A:27,G:M Please keep the response data format."
        String message = "C:" + c + ",A:" + age + ",G:" + gender + " Please keep the response data format.";

        return gptAssistantService.createMessage(threadId, message);
    }

    /**
     * 실행
     */
    @PostMapping("/ai/run")
    public CreateRunResDto createRun(
            @RequestParam("thread_id")
            String threadId,
            @RequestParam("assistant_id")
            String assistantId
    ) {
        return gptAssistantService.runAssistant(threadId, assistantId);
    }

    @GetMapping("/ai/message")
    public GetMessagesResDto getMessages(
            @RequestParam("thread_id")
            String threadId
    ) {
        return gptAssistantService.getMessages(threadId);
    }

}
