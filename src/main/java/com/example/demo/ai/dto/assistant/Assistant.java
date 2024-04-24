package com.example.demo.ai.dto.assistant.v2;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
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

    private Integer temperature;

    @JsonProperty("top_p")
    private Integer topP;

}
