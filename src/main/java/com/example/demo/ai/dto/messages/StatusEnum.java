package com.example.demo.ai.dto.messages;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum StatusEnum {
        @JsonProperty("in_progress")
        IN_PROGRESS,

        @JsonProperty("incomplete")
        IN_COMPLETE,

        @JsonProperty("completed")
        COMPLETED
}
