package com.example.demo.ai.dto.assistant.v2;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class ExpiredAfter {
    private String anchor;
    private Integer days;
}
