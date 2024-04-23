package com.example.demo.ai.dto.assistant;

import com.example.demo.ai.dto.run.CreateRunResDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CreateAssistantReqDto {
    private String instructions;
    private String name;
    private String model;
    private List<CreateRunResDto.Tool> tools;
}
