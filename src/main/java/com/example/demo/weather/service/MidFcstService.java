package com.example.demo.weather.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class MidFcstService {
    private final MidFcstApiService midFcstApiService;

    @Value("${Fcst.serviceKey}")
    private String serviceKey;

    // 중기 육상 예보
    public Object getMidLandFcst(
            String regId
    ) {
        LocalDateTime currentTime = LocalDateTime.now();
        // 일 2회(06:00,18:00)회 생성 되며 발표시각을 입력
        // 최근 24시간 자료만 제공

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HHmm");

        Map<String, Object> params = new HashMap<>();
        params.put("serviceKey", serviceKey);
        params.put("numOfRows", 10);
        params.put("pageNo", 1);
        params.put("dataType", "JSON");
        params.put("regId", regId);      // 예보구역코드
        params.put("tmFc", "202404231800");   // 발표 시각

        return midFcstApiService.MidLandFcst(params);
    }


    // 중기 기온 조회
    public Object getMidTa(
            String regId
    ) {
        LocalDateTime currentTime = LocalDateTime.now();
        // 일 2회(06:00,18:00)회 생성 되며 발표시각을 입력
        // 최근 24시간 자료만 제공

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HHmm");

        Map<String, Object> params = new HashMap<>();
        params.put("serviceKey", serviceKey);
        params.put("numOfRows", 10);
        params.put("pageNo", 1);
        params.put("dataType", "JSON");
        params.put("regId", regId);      // 예보구역코드
        params.put("tmFc", "202404231800");   // 발표 시각

        return midFcstApiService.MidTa(params);
    }
}
