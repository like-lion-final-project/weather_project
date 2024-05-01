package com.example.demo.ai.dto.file;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class FileData {
    @JsonProperty("id")
    private String id;

    @JsonProperty("object")
    private String object;

    @JsonProperty("bytes")
    private Long bytes;

    @JsonProperty("created_at")
    private Long createdAt;

    @JsonProperty("filename")
    private String fileName;

    @JsonProperty("purpose")
    private String purpose;
}


