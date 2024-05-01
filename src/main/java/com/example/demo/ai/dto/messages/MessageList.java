package com.example.demo.ai.dto.messages;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class MessageList {
    private String id;
    private String object;

    @JsonProperty("data")
    private List<Message> messages;

    @JsonProperty("first_id")
    private String firstId;

    @JsonProperty("last_id")
    private String lastId;

    @JsonProperty("has_more")
    private boolean hasMore;

}
