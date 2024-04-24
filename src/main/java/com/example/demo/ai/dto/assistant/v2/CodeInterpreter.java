package com.example.demo.ai.dto.assistant.v2;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class CodeInterpreter {
    @JsonProperty("file_ids")
    private List<String> fileIds;
}
