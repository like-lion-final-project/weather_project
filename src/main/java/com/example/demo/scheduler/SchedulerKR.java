package com.example.demo.scheduler;

import com.example.demo.ai.dto.run.CreateThreadAndRunRequest;
import com.example.demo.ai.dto.run.CreateThreadAndRunRequestThread;
import com.example.demo.ai.entity.AssistantEntity;
import com.example.demo.ai.repo.AssistantRepo;
import com.example.demo.ai.service.GptService;
import com.example.demo.ai.service.dto.DailyCodyResDto;
import com.example.demo.weather.dto.WeatherForecast;
import com.example.demo.weather.dto.fcst.FcstItem;
import com.example.demo.weather.service.VilageFcstApiService;
import com.example.demo.weather.service.VilageSrtFcstService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class SchedulerKR {
    private final GptService gptService;
    private final AssistantRepo assistantRepo;
    private final VilageSrtFcstService vilageFcstApiService;


/**
 * <p>한국시간 기준 스케줄러 입니다.</p>
 *<p>1 2 3 4 5 6 <br />
 *   *  *  *  *  *  * <br />
 *
 *  1. 초(0-59) <br />
 *  2. 분(0-59) <br />
 *  3. 시간(0-23) <br />
 *  4. 일(1-31) <br />
 *  5. 월(1-12) <br />
 *  6. 요일(0-7)
 * </p>
 *

 * */
// @Scheduled(cron = "10 * * * * *")
//@Scheduled(fixedDelay = 9000)
    public void dailyCodyScheduler(){
     log.info("cron - start");
     Optional<AssistantEntity> assistant = assistantRepo.findAssistantByAssistantTypeAndVersion("fashion","0.0.1");
     if(assistant.isEmpty()){
         return;
     }
      List<WeatherForecast> weatherForecasts = vilageFcstApiService.getUltraSrtFcst(63,120);

     List<FcstItem> fcstItems = new ArrayList<>();
     for (WeatherForecast item: weatherForecasts
          ) {
         FcstItem fcstItem = new FcstItem();
              fcstItem.setFcstTime(item.getFcstTime());
              fcstItem.setFcstValue(item.getForecastValues().get("T1H") );
     }

     DailyCodyResDto dailyCodyResDto = gptService.generateDailyCodyCategory(fcstItems);
     for (String category:dailyCodyResDto.getCategories()
          ) {
         System.out.println(category + " : 오늘의 추천 카테고리");
     }
     log.info("cron - end");

    }
}
