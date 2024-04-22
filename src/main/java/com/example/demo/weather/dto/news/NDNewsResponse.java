package com.example.demo.weather.dto.news;

import java.util.List;
import lombok.Data;

@Data
public class NDNewsResponse {
    private String lastBuildDate;
    private int total;
    private int start;
    private int display;
    private List<NDNewsItem> items;
}
