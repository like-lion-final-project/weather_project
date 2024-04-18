package com.example.demo.ai.dto.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import jdk.jfr.SettingDefinition;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class CreateThreadAndRunReqDto {
    @JsonProperty("assistant_id")
    private String assistantId;

    private Thread thread;


    @Getter
    @Setter
    @Builder
    @RequiredArgsConstructor
    @AllArgsConstructor
    public static class Thread {
        @JsonProperty("messages")
        private List<CreateMessageDto> messages;
    }

}
