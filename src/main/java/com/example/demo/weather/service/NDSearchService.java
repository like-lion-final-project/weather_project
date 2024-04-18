package com.example.demo.weather.service;

import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NDSearchService {
    private final NDApiService ndApiService;

    public Object ndNewsSearch(
        Integer start
    ) {
        Map<String, Object> params = new HashMap<>();
        params.put("query", "날씨예보");
        params.put("display", 5);
        params.put("start", start);
        return ndApiService.newsSearch(params);
    }
}
