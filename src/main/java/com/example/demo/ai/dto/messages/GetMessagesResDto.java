package com.example.demo.ai.dto.messages;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class GetMessagesResDto {
    private String object;


    @JsonProperty("data")
    private List<CreateMessageResDto> data;

    @JsonProperty("has_more")
    private boolean hasMore;

    @JsonProperty("first_id")
    private String firstId;

    @JsonProperty("last_id")
    private String lastId;
}
