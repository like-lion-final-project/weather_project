package com.example.demo.ai.dto.run.v2;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class TruncationStrategy {
    private String type;
    @JsonProperty("last_messages")
    private String lastMessages;

    // Getters and Setters
}