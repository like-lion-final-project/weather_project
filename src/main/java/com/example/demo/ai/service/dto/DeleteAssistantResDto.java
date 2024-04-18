package com.example.demo.ai.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class DeleteAssistantResDto {
    private String id;
    private String object;
    private boolean deleted;
}
