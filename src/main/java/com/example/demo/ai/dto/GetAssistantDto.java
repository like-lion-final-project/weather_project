package com.example.demo.ai.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetAssistantDto {
    String object;
    Data data;
    String firstId;
    String lastId;
    Boolean hasMore;

    @Getter
    public class Data {
        @JsonProperty("id")
        String id;

        @JsonProperty("object")
        String object;

        @JsonProperty("created_at")
        String createdAt;

        @JsonProperty("name")
        String name;

        @JsonProperty("description")
        String description;

        @JsonProperty("model")
        String model;

        @JsonProperty("instructions")
        String instructions;

        @JsonProperty("tools")
        String tools;

        @JsonProperty("top_p")
        String topP;

        @JsonProperty("temperature")
        String temperature;

        @JsonProperty("file_ids")
        String fileIds;

        @JsonProperty("metadata")
        String metadata;

        @JsonProperty("response_format")
        String responseFormat;
    }
}
