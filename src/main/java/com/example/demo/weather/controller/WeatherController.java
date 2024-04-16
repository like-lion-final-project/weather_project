package com.example.demo.weather.controller;

import com.example.demo.weather.api.service.FcstApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/weather")
@RequiredArgsConstructor
public class WeatherController {
    private final FcstApiService fcstApiService;

    // (nx, ny) 위치의 날씨 조회 테스트 (시간 : 4/16 23:00)
    @GetMapping
    public Object weatherFcst(
            @RequestParam("nx")
            Integer nx,
            @RequestParam("ny")
            Integer ny
    ) {
        return fcstApiService.getVilageFcst(nx, ny);
    }
}
