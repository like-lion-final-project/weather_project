package com.example.demo.ai.service.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DailyCodyReqDto {
    private String fcstTime;
    private String fcstValue;
}
