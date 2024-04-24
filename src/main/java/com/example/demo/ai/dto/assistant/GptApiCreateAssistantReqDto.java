package com.example.demo.ai.dto.assistant;

import com.example.demo.ai.dto.Tool;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class GptApiCreateAssistantReqDto {
    private String instructions;
    private String name;
    private String model;
    private List<Tool> tools;
}
