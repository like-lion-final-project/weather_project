package com.example.demo.ai.dto.assistant;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class Tool{
    @JsonProperty("type")
    private ToolTypeEnum type;
}
