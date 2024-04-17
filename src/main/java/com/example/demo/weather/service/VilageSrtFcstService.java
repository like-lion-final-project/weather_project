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
public class VilageSrtFcstService {
    private final VilageFcstApiService vilageFcstApiService;

    @Value("${Fcst.serviceKey}")
    private String serviceKey;

    // 단기 예보 조회
    public Object getVilageFcst(
            Double nx,
            Double ny
    ) {
        LocalDateTime currentTime = LocalDateTime.now().minusHours(1);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HHmm");

        Map<String, Object> params = new HashMap<>();
        params.put("serviceKey", serviceKey);
        params.put("numOfRows", 266);
        params.put("pageNo", 1);
        params.put("dataType", "JSON");
        params.put("base_date", dateTimeFormatter.format(currentTime));  // 발표 일자
        params.put("base_time", timeFormatter.format(currentTime));      // 발표 시각 (1시간전)
        params.put("nx", nx);                                            // 예보지점 x좌표값
        params.put("ny", ny);                                            // 예보지점 y좌표값
         log.info("발표 일자: {}", dateTimeFormatter.format(currentTime));
         log.info("발표 시각: {}", timeFormatter.format(currentTime));

        return vilageFcstApiService.getVilageFcst(params);
    }

}
