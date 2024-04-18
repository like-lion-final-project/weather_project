package com.example.demo.ai.dto.message;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class CreateThreadAndRunResDto {
    @JsonProperty("id")
    private String id;

    @JsonProperty("object")
    private String object;

    @JsonProperty("created_at")
    private Long createdAt;

    @JsonProperty("assistant_id")
    private String assistantId;

    @JsonProperty("thread_id")
    private String threadId;

    @JsonProperty("status")
    private String status;

    @JsonProperty("started_at")
    private Long startedAt;

    @JsonProperty("expires_at")
    private Long expiresAt;

    @JsonProperty("cancelled_at")
    private Long cancelledAt;

    @JsonProperty("failed_at")
    private Long failedAt;

    @JsonProperty("completed_at")
    private Long completedAt;

    @JsonProperty("required_action")
    private String requiredAction;

    @JsonProperty("last_error")
    private String lastError;

    @JsonProperty("model")
    private String model;

    @JsonProperty("instructions")
    private String instructions;

    @JsonProperty("tools")
    private List<String> tools;

    @JsonProperty("tool_resources")
    private Map<String, Object> toolResources;

    @JsonProperty("metadata")
    private Map<String, Object> metadata;

    @JsonProperty("temperature")
    private Double temperature;

    @JsonProperty("top_p")
    private Double topP;

    @JsonProperty("max_completion_tokens")
    private Integer maxCompletionTokens;

    @JsonProperty("max_prompt_tokens")
    private Integer maxPromptTokens;

    @JsonProperty("truncation_strategy")
    private TruncationStrategy truncationStrategy;

    @JsonProperty("incomplete_details")
    private Object incompleteDetails;

    @JsonProperty("usage")
    private Object usage;

    @JsonProperty("response_format")
    private String responseFormat;

    @JsonProperty("tool_choice")
    private String toolChoice;

    @JsonProperty("file_ids")
    private List<String> fileIds = new ArrayList<>();  // 초기화

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @RequiredArgsConstructor
    public static class TruncationStrategy {
        @JsonProperty("type")
        private String type;

        @JsonProperty("last_messages")
        private Integer lastMessages = null;
    }
}