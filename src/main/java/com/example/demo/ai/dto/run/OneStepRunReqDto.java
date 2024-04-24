package com.example.demo.ai.dto.run;

import com.example.demo.ai.dto.message.CreateMessageDto;
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

    @Getter
    @Builder
    @RequiredArgsConstructor
    @AllArgsConstructor
    public static class Thread {
        private List<CreateMessageDto> messages;
    }
}
