package com.example.demo.ai.dto.run;

import com.example.demo.ai.dto.Tool;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;


@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class Run {
    private String id;
    private String object;

    @JsonProperty("created_at")
    private long createdAt;

    @JsonProperty("assistant_id")
    private String assistantId;

    @JsonProperty("thread_id")
    private String threadId;
    private String status;

    @JsonProperty("started_at")
    private Long startedAt;

    @JsonProperty("expires_at")
    private long expiresAt;

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
    private String model;
    private String instructions;

    @JsonProperty("tools")
    private List<Tool> tools;

    @JsonProperty("file_ids")
    private List<String> fileIds;
    private Object metadata;
    private double temperature;

    @JsonProperty("top_p")
    private double topP;

    @JsonProperty("max_completion_tokens")
    private Integer maxCompletionTokens;

    @JsonProperty("max_prompt_tokens")
    private Integer maxPromptTokens;

    @JsonProperty("truncation_strategy")
    private TruncationStrategy truncationStrategy;

    @JsonProperty("incomplete_details")
    private String incompleteDetails;
    private Object usage;

    @JsonProperty("response_format")
    private String responseFormat;

    @JsonProperty("tool_choice")
    private String toolChoice;

    @Getter
    @Setter
    @RequiredArgsConstructor
    @AllArgsConstructor
    public static class TruncationStrategy {
        private String type;
        @JsonProperty("last_messages")
        private String lastMessages;

        // Getters and Setters
    }
}
