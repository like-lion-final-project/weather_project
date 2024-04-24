package com.example.demo.ai.dto.messages;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class CreateMessageResDto {

    @JsonProperty("id")
    private String id;

    @JsonProperty("object")
    private String object;

    @JsonProperty("created_at")
    private Long createdAt;

    @JsonProperty("assistant_id")
    private String assistantId;

    @JsonProperty("thread_id")
    private String threadId;

    @JsonProperty("run_id")
    private String runId;

    @JsonProperty("role")
    private String role;

    @JsonProperty("content")
    private List<Content> content;


    @Getter
    @Setter
    @RequiredArgsConstructor
    @AllArgsConstructor
    public static class Content {
        @JsonProperty("type")
        String type;

        @JsonProperty("text")
        Text text;

        @Getter
        @Setter
        @RequiredArgsConstructor
        @AllArgsConstructor
        public static class Text{

            @JsonProperty("value")
            String value;

            @JsonProperty("annotations")
            Object annotations;
        }


    }

    @JsonProperty("file_ids")
    private List<String> fileIds = new ArrayList<>();  // 초기화

    @JsonProperty("metadata")
    Object metadata;
}
