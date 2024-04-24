package com.example.demo.ai.dto.assistant;

import com.example.demo.ai.dto.Tool;
import com.example.demo.ai.dto.run.CreateRunResDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class CreateAssistantReqDto {
    private String instructions;
    private String name;
    private String model;

    @JsonProperty("tools")
    private List<Tool> tools;
    private String version;
    @JsonProperty("assistant_type")
    private String assistantType;
}
