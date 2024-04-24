package com.example.demo.ai.dto.assistant;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class AssistantCreateRequest {
    private String instructions;
    private String name;
    private String model;

    @JsonProperty("tools")
    private List<Tool> tools;

    @JsonProperty("tool_resources")
    private ToolsResources toolsResources;
}
