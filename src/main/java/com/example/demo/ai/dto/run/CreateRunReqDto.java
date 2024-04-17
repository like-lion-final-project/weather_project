package com.example.demo.ai.dto.run;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class CreateRunReqDto {
    @JsonProperty("assistant_id")
    String assistantId;
}
