package com.example.demo.ai.dto.assistant;
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
public class GetAssistantResDto {

    @JsonProperty("object")
    private String object;

    @JsonProperty("first_id")
    private String firstId;

    @JsonProperty("last_id")
    private String lastId;

    @JsonProperty("has_more")
    private Boolean hasMore;

    @JsonProperty("data")
    private List<Data> data;

    @Getter
    @Builder
    @RequiredArgsConstructor
    @AllArgsConstructor
    public static class Data {

        @JsonProperty("id")
        private String id;

        @JsonProperty("object")
        private String object;

        @JsonProperty("created_at")
        private Long createdAt;

        @JsonProperty("name")
        private String name;

        @JsonProperty("description")
        private String description;

        @JsonProperty("model")
        private String model;

        @JsonProperty("instructions")
        private String instructions;

        @JsonProperty("tools")
        private List<String> tools;

        @JsonProperty("top_p")
        private Double topP;

        @JsonProperty("temperature")
        private Double temperature;

        @JsonProperty("file_ids")
        private List<String> fileIds;

        @JsonProperty("metadata")
        private Object metadata;

        @JsonProperty("response_format")
        private String responseFormat;
    }
}