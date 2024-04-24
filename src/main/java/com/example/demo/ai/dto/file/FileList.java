package com.example.demo.ai.dto.file;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class FileList {
    private List<FileData> data;
    private String object;

    // Getters and setters
    public List<FileData> getData() {
        return data;
    }

    public void setData(List<FileData> data) {
        this.data = data;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }
}
