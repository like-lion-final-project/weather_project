package com.example.demo.ai.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateAssistantReqDto {
    private String instructions;
    private String name;
    private String model;
}
