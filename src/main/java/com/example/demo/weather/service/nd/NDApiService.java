package com.example.demo.weather.service.nd;

import com.example.demo.weather.dto.news.NDNewsResponse;
import java.util.Map;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;


public interface NDApiService {
    @GetExchange("v1/search/news")
    NDNewsResponse newsSearch(
            @RequestParam
            Map<String, Object> params
    );
}
