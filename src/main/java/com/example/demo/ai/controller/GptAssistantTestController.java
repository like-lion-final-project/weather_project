package com.example.demo.ai.controller;


import com.example.demo.ai.dto.assistant.CreateAssistantReqDto;
import com.example.demo.ai.dto.assistant.GetAssistantResDto;
import com.example.demo.ai.dto.assistant.GptApiCreateAssistantResDto;
import com.example.demo.ai.dto.file.FileData;
import com.example.demo.ai.dto.file.FileDelete;
import com.example.demo.ai.dto.file.FileList;
import com.example.demo.ai.dto.message.CreateMessageDto;
import com.example.demo.ai.dto.message.CreateMessageResDto;
import com.example.demo.ai.dto.message.GetMessagesResDto;
import com.example.demo.ai.dto.run.CreateRunReqDto;
import com.example.demo.ai.dto.run.CreateRunResDto;
import com.example.demo.ai.dto.run.OneStepRunParamDto;
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
    public CreateRunResDto createRun(
            @PathVariable("threadId")
            String threadId,
            @RequestBody
            CreateRunReqDto dto
    ) {
        return gptAssistantApiService.run(threadId, dto.getAssistantId());
    }

    @GetMapping("/v1/threads/{threadId}/runs/{runId}")
    public CreateRunResDto getRun(
            @PathVariable("threadId")
            String threadId,
            @PathVariable("runId")
            String runId
    ) {
        return gptAssistantApiService.getRun(threadId, runId);
    }

    @PostMapping("/v1/threads/runs")
    public CreateRunResDto onStepRun(
            @RequestBody
            OneStepRunParamDto dto

    ) {
        return gptAssistantApiService.oneStepRun(dto.getRole(), dto.getMessage(), dto.getAssistantId());
    }

    @PostMapping("/v1/daily-cody")
    public void dailyCody(
            @RequestBody
            OneStepRunParamDto dto
    ) {

        List<FcstItem> fcstItemList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            FcstItem fcstItem = new FcstItem();
            fcstItem.setBaseDate("20220423");
            fcstItem.setBaseTime("0500");
            fcstItem.setCategory("none");
            fcstItem.setFcstDate("20220423");
            fcstItem.setFcstTime(i + ":00");
            fcstItem.setFcstValue(i + 1 + "");
            fcstItem.setNx(i);
            fcstItem.setNy(i);
            fcstItemList.add(
                    fcstItem
            );
        }

        DailyCodyResDto dailyCodyResDto = gptService.generateDailyCodyCategory(fcstItemList);
        for (String item:dailyCodyResDto.getCategories()
             ) {
            System.out.println(item + "카테고리");
        }
    }

    @PostMapping("/v1/files")
    public FileData uploadFile(
            @RequestParam("uploadFile")
                               MultipartFile uploadFile,
                           HttpServletRequest req

    ){

        return gptAssistantApiService.fileUploadAPI(uploadFile);

    }

    @GetMapping("/v1/files")
    public Optional<FileList> getFiles(){
        return gptAssistantApiService.getfiles();
    }

    @DeleteMapping("/v1/files/{fileId}")
    public Optional<FileDelete> deleteFile(
            @PathVariable("fileId")
            String fileId
    ){
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


