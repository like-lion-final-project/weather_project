package com.example.demo.weather.controller;

import com.example.demo.weather.dto.MidLandDto;
import com.example.demo.weather.dto.MidTaDto;
import com.example.demo.weather.service.MidAreaCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/area-code")
@RequiredArgsConstructor
public class MidAreaCodeController {
    private final MidAreaCodeService service;

    // 중기 육상 예보구역코드 조회
    @GetMapping("/mid-land")
    public MidLandDto readLandAreaCode(
            @RequestParam("area")
            String area
    ) {
        return service.readLandAreaCode(area);
    }

    // 중기 기상 예보구역코드 조회
    @GetMapping("/mid-ta")
    public MidTaDto readTaAreaCode(
            @RequestParam("area")
            String area
    ) {
        return service.readTaAreaCode(area);
    }

}
