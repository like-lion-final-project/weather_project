package com.example.demo.ai.dto.assistant;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ToolTypeEnum {
    @JsonProperty("code_interpreter")
    CODEINTERPRETER,
    @JsonProperty("file_search")
    FILESEARCH
}
