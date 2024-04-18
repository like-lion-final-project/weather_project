package com.example.demo.ai.dto.thread;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class DeleteThreadResDto {
    private String id;
    private String object;
    private boolean deleted;
}
