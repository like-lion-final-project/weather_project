package com.example.demo.ai.dto.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

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

    @JsonProperty("role_id")
    private String roleId;


    @Getter
    @Setter
    @RequiredArgsConstructor
    @AllArgsConstructor
    public class Content {
        @JsonProperty("type")
        String type;

        @JsonProperty("text")
        Text text;

        @Getter
        @Setter
        @RequiredArgsConstructor
        @AllArgsConstructor
        public class Text{

            @JsonProperty("value")
            String value;

            @JsonProperty("annotations")
            Object annotations;
        }
        @JsonProperty("file_ids")
        List<String> fileIds;

        @JsonProperty("metadata")
        Object metadata;
    }

}
