package com.example.demo.ai.dto.thread;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class CreateThreadResDto {
    private String id;
    private String objectt123;

    @JsonProperty("created_at")
    private Long createdAt;
    private Object metadata;
}
