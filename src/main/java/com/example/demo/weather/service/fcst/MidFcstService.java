package com.example.demo.weather.service.fcst;

import com.example.demo.weather.dto.mid_land.MidLandApiResponse;
import com.example.demo.weather.dto.mid_ta.MidTaApiResponse;
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
    public MidLandApiResponse getMidLandFcst(
            String regId
    ) {
        LocalDateTime currentTime = LocalDateTime.now();
        // 일 2회(06:00,18:00)회 생성 되며 발표시각을 입력
        // 최근 24시간 자료만 제공

        int currentHour = currentTime.getHour();
        int targetHour = currentHour >= 18 ? 18 : 6; // 현재 시간이 18시보다 크거나 같으면 18시로, 아니면 6시로 설정

        LocalDateTime targetDateTime = LocalDateTime.of(
                currentTime.getYear(),
                currentTime.getMonth(),
                currentTime.getDayOfMonth(),
                targetHour,
                0
        );
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
        String tmFc = targetDateTime.format(dateTimeFormatter);

        Map<String, Object> params = new HashMap<>();
        params.put("serviceKey", serviceKey);
        params.put("numOfRows", 10);
        params.put("pageNo", 1);
        params.put("dataType", "JSON");
        params.put("regId", regId);      // 예보구역코드
        params.put("tmFc", tmFc);   // 발표 시각

        return midFcstApiService.MidLandFcst(params);
    }


    // 중기 기온 조회
    public MidTaApiResponse getMidTa(
            String regId
    ) {
        LocalDateTime currentTime = LocalDateTime.now();
        // 일 2회(06:00,18:00)회 생성 되며 발표시각을 입력
        // 최근 24시간 자료만 제공

        int currentHour = currentTime.getHour();
        int targetHour = currentHour >= 18 ? 18 : 6; // 현재 시간이 18시보다 크거나 같으면 18시로, 아니면 6시로 설정

        LocalDateTime targetDateTime = LocalDateTime.of(
                currentTime.getYear(),
                currentTime.getMonth(),
                currentTime.getDayOfMonth(),
                targetHour,
                0
        );
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
        String tmFc = targetDateTime.format(dateTimeFormatter);

        Map<String, Object> params = new HashMap<>();
        params.put("serviceKey", serviceKey);
        params.put("numOfRows", 10);
        params.put("pageNo", 1);
        params.put("dataType", "JSON");
        params.put("regId", regId);      // 예보구역코드
        params.put("tmFc", tmFc);   // 발표 시각

        return midFcstApiService.MidTa(params);
    }
}
