package com.example.demo.ai.dto.file;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class FileDelete {
    private String id;
    private String object;
    private boolean deleted;
}
