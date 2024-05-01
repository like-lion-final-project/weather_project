package com.example.demo.ai.dto.assistant;

import com.example.demo.ai.dto.assistant.Tool;
import com.example.demo.ai.dto.assistant.ToolsResources;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class Assistant {
    private String id;
    private String object;

    @JsonProperty("created_at")
    private Integer createdAt;
    private String name;
    private String description;
    private String model;

    private String instructions;

    @JsonProperty("tools")
    private List<Tool> tools;

    @JsonProperty("tool_resources")
    private ToolsResources toolsResources;

    private Map<String,String> metadata;

    @JsonProperty("response_format")
    private ResponseFormat responseFormat;

    private Integer temperature;

    @JsonProperty("top_p")
    private Integer topP;
}
