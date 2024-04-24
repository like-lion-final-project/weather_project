package com.example.demo.ai.dto.assistant.v2;

import com.example.demo.ai.dto.messages.StatusEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class VectorStore {
    private String id;
    private String object;

    @JsonProperty("created_at")
    private Integer createdAt;

    private String name;

    @JsonProperty("usageBytes")
    private Integer usageBytes;

    @JsonProperty("file_counts")
    private FileCount fileCount;

    private StatusEnum status;

    @JsonProperty("expired_after")
    private ExpiredAfter expiredAfter;

    @JsonProperty("expires_at")
    private Integer expiresAt;

    @JsonProperty("last_active_at")
    private Integer lastActiveAt;
    private Map<String,String> metadata;

}
