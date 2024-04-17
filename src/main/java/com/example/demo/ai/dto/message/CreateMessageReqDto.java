package com.example.demo.ai.dto.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class CreateMessageReqDto {

    @JsonProperty("role")
    private String role;

    @JsonProperty("content")
    private String content;
}
