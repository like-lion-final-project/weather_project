package com.example.demo.ai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class GetModelsResDto {
    @JsonProperty("object")
    public String object;
    public ArrayList<Data> data;

    @Getter
    @Setter
    @AllArgsConstructor
    @RequiredArgsConstructor
    public static class Data {

        @JsonProperty("id")
        public String id;

        @JsonProperty("object")
        public String object;

        @JsonProperty("created")
        public Long created;

        @JsonProperty("owned_by")
        public String ownedBy;
    }
}
