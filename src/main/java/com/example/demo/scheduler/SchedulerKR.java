package com.example.demo.scheduler;

import com.example.demo.ai.dto.run.CreateThreadAndRunRequest;
import com.example.demo.ai.dto.run.CreateThreadAndRunRequestThread;
import com.example.demo.ai.entity.AssistantEntity;
import com.example.demo.ai.repo.AssistantRepo;
import com.example.demo.ai.service.GptService;
import com.example.demo.ai.service.dto.DailyCodyResDto;
import com.example.demo.cody.dto.ItemDto;
import com.example.demo.cody.entity.ClothsCategory;
import com.example.demo.cody.entity.DailySuggestion;
import com.example.demo.cody.repo.ClothsCategoryRepository;
import com.example.demo.cody.repo.DailySuggestionRepository;
import com.example.demo.cody.service.NaverSearchService;
import com.example.demo.utils.NaverShopSearch;
import com.example.demo.weather.dto.WeatherForecast;
import com.example.demo.weather.dto.srt_fcst.FcstItem;
import com.example.demo.weather.service.fcst.SrtFcstService;
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
    private final SrtFcstService srtFcstService;
    private final DailySuggestionRepository dailySuggestionRepo;
    private final ClothsCategoryRepository clothsCategoryRepo;
    private final NaverShopSearch naverShopSearch;
    private final NaverSearchService naverSearchService;


    /**
     * <p>한국시간 기준 스케줄러 입니다.</p>
     * <p>1 2 3 4 5 6 <br />
     * *  *  *  *  *  * <br />
     * <p>
     * 1. 초(0-59) <br />
     * 2. 분(0-59) <br />
     * 3. 시간(0-23) <br />
     * 4. 일(1-31) <br />
     * 5. 월(1-12) <br />
     * 6. 요일(0-7)
     * </p>
     */
// @Scheduled(cron = "10 * * * * *")
    @Scheduled(fixedDelay = 3000)
    public void dailyCodyScheduler() {
        log.info("cron - start");
        Optional<AssistantEntity> assistant = assistantRepo.findAssistantByAssistantTypeAndVersion("fashion", "0.0.2");
        if (assistant.isEmpty()) {
            return;
        }
        List<WeatherForecast> weatherForecasts = srtFcstService.getUltraSrtFcst(63, 120);

        // 날씨 정보 가져오기
        List<FcstItem> fcstItems = new ArrayList<>();
        for (WeatherForecast item : weatherForecasts) {
            FcstItem fcstItem = new FcstItem();
            fcstItem.setFcstTime(item.getFcstTime());
            fcstItem.setFcstValue(item.getForecastValues().get("T1H"));
        }

        // 추천 코디 요청
        DailyCodyResDto dailyCodyResDto = gptService.generateDailyCodyCategory(fcstItems);
        for (String category : dailyCodyResDto.getCategories() ) {
            Optional<ClothsCategory> clothsCategory = clothsCategoryRepo.findByType(category);

            // 추천 받은 카테고리가 DB에 존재하는 카테고리라면 해당 카테고리로 저장
            if (clothsCategory.isPresent()) {
                dailySuggestionRepo.save(
                        DailySuggestion.builder()
                                .category(clothsCategory.get())
                                .originalQuery(clothsCategory.get().getType())
                                .build());
            } else {
                // DB에 없는 카테고리라면 기타 카테고리로 저장 ( 임시 )
                Optional<ClothsCategory> otherItemsCategory = clothsCategoryRepo.findByType("기타");
                if (otherItemsCategory.isEmpty()) throw new RuntimeException("데이터베이스에 -기타- 카테고리가 존재하지 않습니다.");


                dailySuggestionRepo.save(
                        DailySuggestion.builder()
                                .category(otherItemsCategory.get())
                                .originalQuery(category)
                                .build());
            }

            System.out.println(category + " : 추천 카테고리");

        }
        log.info("cron - end");
    }


}
