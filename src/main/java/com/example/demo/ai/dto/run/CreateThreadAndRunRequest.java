package com.example.demo.ai.dto.run;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class CreateThreadAndRunRequest {
    @JsonProperty("assistant_id")
    private String assistantId;
    private CreateThreadAndRunRequestThread thread;
    private String model;
//    private String instructions;
//
//    @JsonProperty("tools")
//    private List<Tool> tools;
//
//    @JsonProperty("tools_resources")
//    private ToolsResources toolsResources;
//
//    private Map<String,String> metadata;
//    private Integer temperature;
//
//    @JsonProperty("top_p")
//    private Integer topP;
//
//    @JsonProperty("stream")
//    private boolean stream;
//
//    @JsonProperty("max_prompt_tokens")
//    private Integer maxPromptTokens;
//
//    @JsonProperty("max_completion_tokens")
//    private Integer maxCompletionTokens;
//
//    @JsonProperty("truncation_strategy")
//    private TruncationStrategy truncationStrategy;
}
