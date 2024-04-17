package com.example.demo.weather.service;

import java.util.Map;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange("/1360000/VilageFcstInfoService_2.0/")
public interface VilageFcstApiService {

    // 초단기 예보 조회
    @GetExchange("/getUltraSrtFcst")
    Object UltraSrtFcst(@RequestParam Map<String, Object> params);

    // 초단기 실황 조회
    @GetExchange("/getUltraSrtNcst")
    Object UltraSrtNcst(@RequestParam Map<String, Object> params);

}



