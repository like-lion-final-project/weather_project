package com.example.demo.ai.controller;


import com.example.demo.ai.dto.assistant.CreateAssistantReqDto;
import com.example.demo.ai.dto.assistant.GetAssistantResDto;
import com.example.demo.ai.dto.assistant.GptApiCreateAssistantResDto;
import com.example.demo.ai.dto.file.FileData;
import com.example.demo.ai.dto.file.FileDelete;
import com.example.demo.ai.dto.file.FileList;
import com.example.demo.ai.dto.messages.CreateMessageResDto;
import com.example.demo.ai.dto.messages.GetMessagesResDto;
import com.example.demo.ai.dto.messages.v2.messages.Message;
import com.example.demo.ai.dto.run.CreateThreadAndRunRequest;
import com.example.demo.ai.dto.run.RunCreateRequest;
import com.example.demo.ai.dto.run.v2.Run;
import com.example.demo.ai.dto.thread.CreateThreadResDto;
import com.example.demo.ai.dto.thread.DeleteThreadResDto;
import com.example.demo.ai.service.GptAssistantApiService;
import com.example.demo.ai.service.GptAssistantApiServiceV2;
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

public class GptAssistantTestControllerV2 {
    private final GptService gptService;
    private final GptAssistantApiServiceV2 gptAssistantApiServiceV2;
    @PostMapping("/v1/daily-cody")
    public void dailyCody(
            @RequestBody
            CreateThreadAndRunRequest dto
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

        DailyCodyResDto dailyCodyResDto = gptService.generateDailyCodyCategory(fcstItemList, dto.getTools(),dto.getToolsResources());
        for (String item : dailyCodyResDto.getCategories()
        ) {
            System.out.println(item + "카테고리");
        }
    }



    @PostMapping("/v2/threads/runs")
    public Run createThreadAndRun(
            @RequestBody
            CreateThreadAndRunRequest dto

    ) {
        List<String> vectorStroeIds = new ArrayList<>();
        vectorStroeIds.add("file-9iyf0ykNAidZl6CzJWXF0QpC");
        return gptAssistantApiServiceV2.createThreadAndRun(dto.getRole(), dto.getMessage(), dto.getAssistantId(), dto.getTools(), dto.getToolsResources());
    }
}

// TODO: 파일 업로드
// TODO: 백터스토어 생성
// TODO: 업로드한 파일 백터스토어에 추가
// TODO: 실행시 해당 백터 스토어 id 추가하여 전송 혹은 어시스턴트 생성시 백터스토어 아이디 추가하여 생성


/**
 * thread_8EZ9Fm2oxQI0iBuEiK9SX1CG
 * thread_R8aj36ScCZLKvK4drIYtA391
 * thread_LO2j8zzCt0GlGiSxhIe18DGo
 * thread_BnhYznQAq8HoQclYjTpABCDN
 * thread_hxgakXQdvkcb9PS5WVkZE5xN
 */


