package com.example.demo.weather.dto.news;

import lombok.Data;

@Data
public class NDNewsItem {
    private String title;
    private String originallink;
    private String link;
    private String description;
    private String pubDate;
}
