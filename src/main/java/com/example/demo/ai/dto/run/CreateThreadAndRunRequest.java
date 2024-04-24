package com.example.demo.ai.dto.run;

import com.example.demo.ai.dto.Tool;
import com.example.demo.ai.dto.ToolsResources;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CreateThreadAndRunRequest {
    @JsonProperty("assistant_id")
    private String assistantId;
    private String message;
    private String role;

    @JsonProperty("tools")
    private List<Tool> tools;

    @JsonProperty("tools_resources")
    private ToolsResources toolsResources;
}
