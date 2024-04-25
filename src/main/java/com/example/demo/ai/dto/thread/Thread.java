package com.example.demo.ai.dto.thread;

import com.example.demo.ai.dto.assistant.ToolsResources;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class Thread {

    private String id;
    private String object;
    @JsonProperty("created_at")
    private Integer createdAt;
    private ToolsResources toolsResources;

    Map<String,String> metadata;
}
