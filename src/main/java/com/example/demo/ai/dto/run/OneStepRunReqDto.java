package com.example.demo.ai.dto.run;

import com.example.demo.ai.dto.Tool;
import com.example.demo.ai.dto.ToolsResources;
import com.example.demo.ai.dto.messages.v2.messages.Message;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class OneStepRunReqDto {

    @JsonProperty("assistant_id")
    private String assistantId;
    private Thread thread;
    private List<Tool> tools;

    @JsonProperty("tool_resources")
    private ToolsResources toolsResources;

    @Getter
    @Builder
    @RequiredArgsConstructor
    @AllArgsConstructor
    public static class Thread {
        private List<Message> messages;
    }
}
