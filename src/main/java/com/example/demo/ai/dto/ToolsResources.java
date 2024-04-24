package com.example.demo.ai.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ToolsResources {
    @JsonProperty("file_search")
    private FileSearch fileSearch;


    @Getter
    @AllArgsConstructor
    @RequiredArgsConstructor
    public static class FileSearch{
        @JsonProperty("vector_store_ids")
        private List<String> vectorStoreIds;
    }
}
