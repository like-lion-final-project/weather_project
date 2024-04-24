package com.example.demo.ai.dto.assistant;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


/**
 *
 * in_progress
 * integer
 *
 * The number of files that are currently being processed.
 *
 * completed
 * integer
 *
 * The number of files that have been successfully processed.
 *
 * failed
 * integer
 *
 * The number of files that have failed to process.
 *
 * cancelled
 * integer
 *
 * The number of files that were cancelled.
 *
 * total
 * integer
 *
 * The total number of files.
 * */

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class FileCount {
    @JsonProperty("in_progress")
    private Integer inProgress;

    @JsonProperty("completed")
    private Integer completed;

    @JsonProperty("failed")
    private Integer failed;

    @JsonProperty("cancelled")
    private Integer cancelled;

    @JsonProperty("total")
    private Integer total;

}

