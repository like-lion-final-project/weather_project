package com.example.demo.ai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class GetModelsResDto {
    private  String object;
    private List<Data> data;

    @Getter
    @Setter
    @AllArgsConstructor
    @RequiredArgsConstructor
    public class Data {

        @JsonProperty("id")
        private String id;

        @JsonProperty("object")
        private  String object;

        @JsonProperty("created")
        private  Long created;

        @JsonProperty("owned_by")
        private  String ownedBy;
    }
}
