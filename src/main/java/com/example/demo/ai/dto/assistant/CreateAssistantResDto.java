package com.example.demo.ai.dto.assistant;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class CreateAssistantResDto {
    @JsonProperty("id")
    private  String id;

    @JsonProperty("object")
    private  String object;

    @JsonProperty("created_at")
    private Long createdAt;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private  String description;

    @JsonProperty("model")
    private  String model;

    @JsonProperty("instructions")
    private String instructions;

    @JsonProperty("tools")
    private  List<String> tools;

    @JsonProperty("top_p")
    private Long topP;

    @JsonProperty("temperature")
    private Long temperature;

    @JsonProperty("file_ids")
    private List<String> fileIds;

    @JsonProperty("metadata")
    private Object metadata;

    @JsonProperty("response_format")
    private String responseFormat;
}
