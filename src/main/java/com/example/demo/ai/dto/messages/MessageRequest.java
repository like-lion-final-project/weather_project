package com.example.demo.ai.dto.messages;

import com.example.demo.ai.dto.file.Attachments;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class MessageRequest {
    String role = "user";

    String content;

    private Attachments attachments;

    Map<String, String> metadata;
}

