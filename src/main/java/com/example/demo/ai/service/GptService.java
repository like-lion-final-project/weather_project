package com.example.demo.ai.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GptService {
    private final GptAssistantApiService gptAssistantApiService;
    // TODO: 오늘의 추천 코디
    public void dailyCody(){

        // TODO: 어떤 유저인지
        return gptAssistantApiService.oneStepRun();
    }

    // TODO: 기온, 지역, 성별 추천 코디
    public void conditionCody(String temp, String location, String gender){

    }
}
