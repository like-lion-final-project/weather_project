package com.example.demo.ai.dto.file;

import com.example.demo.ai.dto.Tool;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class Attachments {

    @JsonProperty("file_id")
    private String fileId;
    private List<Tool> tools;
}
