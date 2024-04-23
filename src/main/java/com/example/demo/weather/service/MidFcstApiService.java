package com.example.demo.weather.service;

import java.util.Map;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange("/1360000/MidFcstInfoService/")
public interface MidFcstApiService {

    // 중기 육상예보 조회
    @GetExchange("/getMidLandFcst")
    Object MidLandFcst(
            @RequestParam
            Map<String, Object> params
    );

    // 중기 기온 조회
    @GetExchange("/getMidTa")
    Object MidTa (
            @RequestParam
            Map<String, Object> params
    );

}