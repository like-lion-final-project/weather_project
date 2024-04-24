package com.example.demo.ai.dto.assistant;


import com.example.demo.ai.dto.assistant.v2.Tool;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class GptApiCreateAssistantResDto {
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
    private  List<Tool> tools;

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
