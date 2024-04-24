package com.example.demo.ai.dto.messages.v2.messages.content;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * A citation within the message that points to a specific quote from a specific File associated with the
 * assistant or the message. Generated when the assistant uses the "retrieval" tool to search files.
 * <p>
 * https://platform.openai.com/docs/api-reference/messages/object
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FileCitation {
    @JsonProperty("file_id")
    String fileId;
    String quote;
}