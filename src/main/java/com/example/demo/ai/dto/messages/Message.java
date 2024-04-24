package com.example.demo.ai.dto.messages;

import com.example.demo.ai.dto.file.Attachments;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

@Getter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class Message {
    private String id;
    private String object;

    @JsonProperty("created_at")
    private int createdAt;

    @JsonProperty("thread_id")
    private String threadId;

    @JsonProperty("status")
    private StatusEnum status;

    @JsonProperty("incomplete_details")
    private InCompletedDetails inCompletedDetail;

    @JsonProperty("completed_at")
    private Integer completedAt;

    @JsonProperty("incomplete_at")
    private Integer inCompletedAt;

    private String role;
    private List<MessageContent> content;

    @JsonProperty("assistant_id")
    private String assistantId;

    @JsonProperty("run_id")
    private String runId;

    @JsonProperty("attachments")
    private Attachments attachments;

    private Map<String, String> metadata;
}
