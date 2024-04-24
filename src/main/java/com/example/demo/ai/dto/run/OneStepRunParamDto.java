package com.example.demo.ai.dto.run;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OneStepRunParamDto {
    @JsonProperty("assistant_id")
    private String assistantId;
    private String message;
    private String role;
}
