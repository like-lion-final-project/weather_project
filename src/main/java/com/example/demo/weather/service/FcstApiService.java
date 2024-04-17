package com.example.demo.weather.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FcstApiService {
    private final FcstHttpInterface fcstHttpInterface;

    @Value("${Fcst.serviceKey}")
    private String serviceKey;

    // 단기 예보 조회
    public Object getVilageFcst(
            Integer nx,
            Integer ny
    ) {
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HHmm");

        Map<String, Object> params = new HashMap<>();
        params.put("serviceKey", serviceKey);
        params.put("numOfRows", 266);
        params.put("pageNo", 1);
        params.put("dataType", "JSON");
        params.put("base_date", dateTimeFormatter.format(currentTime));  // 발표 일짜
        params.put("base_time", timeFormatter.format(currentTime));      // 발표 시각 (현재 시각으로부터 1시간 후까지로 설정)
        params.put("nx", nx);                                            // 예보지점 x좌표값
        params.put("ny", ny);                                            // 예보지점 y좌표값

        return fcstHttpInterface.getVilageFcst(params);

    }

}
