package com.example.demo.ai.dto.assistant;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class FileSearch{
    @JsonProperty("vector_store_ids")
    private List<String> vectorStoreIds;
}