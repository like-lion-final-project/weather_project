package com.example.demo.weather.service;

import java.util.Map;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange("/1360000/VilageFcstInfoService_2.0/")
public interface VilageFcstApiService {
    // 단기 예보 조회
    @GetExchange("/getVilageFcst")
    Object getVilageFcst(@RequestParam Map<String, Object> params);

}



