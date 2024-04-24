package com.example.demo.weather.service.nd;

import com.example.demo.weather.dto.news.NDNewsResponse;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NDSearchService {
    private final NDApiService ndApiService;

    public NDNewsResponse ndNewsSearch(
        Integer start
    ) {
        Map<String, Object> params = new HashMap<>();
        params.put("query", "날씨예보");
        params.put("display", 5);
        params.put("start", start);
        return ndApiService.newsSearch(params);
    }
}
