package com.example.demo.cody.service;

import com.example.demo.cody.dto.FeedbackDto;
import com.example.demo.cody.dto.ItemDto;
import com.example.demo.cody.entity.ClothsCategory;
import com.example.demo.cody.entity.DailySuggestion;
import com.example.demo.cody.entity.SuggestionFeedback;
import com.example.demo.cody.repo.ClothsCategoryRepository;
import com.example.demo.cody.repo.DailySuggestionRepository;
import com.example.demo.cody.repo.SuggestionFeedbackRepository;
import lombok.AllArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class NaverSearchService {
    private final SuggestionFeedbackRepository suggetionFeedbackRepository;
    private final ClothsCategoryRepository clothsCategoryRepository;
    private final DailySuggestionRepository dailySuggestionRepository;
    public List<ItemDto> fromJSONtoItems(String result) {
        JSONObject rjson = new JSONObject(result);
        System.out.println(rjson);
        JSONArray items = rjson.getJSONArray("items");
        List<ItemDto> itemDtoList = new ArrayList<>();
        for (int i = 0; i < items.length(); i++) {
            JSONObject itemJson = items.getJSONObject(i);
            ItemDto itemDto = new ItemDto(itemJson);
            itemDtoList.add(itemDto);
        }
        return itemDtoList;
    }

    public void saveFeedback(FeedbackDto feedbackDto) {

        // 피드백의 카테고리를 찾기
        Optional<ClothsCategory> optionalCategory = clothsCategoryRepository.findByType(feedbackDto.getCategory());
        ClothsCategory category = optionalCategory.orElseGet(() -> {
            feedbackDto.setCategory("기타");
            return clothsCategoryRepository.findByType("기타").orElse(null);
        });
// 피드백 엔티티를 생성하여 저장.
        SuggestionFeedback suggestionFeedback = SuggestionFeedback.builder()
                .rating(feedbackDto.getRating())
                .image(feedbackDto.getImage())
                .category(category)
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .build();
        suggetionFeedbackRepository.save(suggestionFeedback);
        }

    // DailySuggestionService에 DailySuggestionRepository 주입

    public String getCategoryTypeOfLastSuggestion() {
        // DailySuggestion 엔티티에서 마지막 추천의 카테고리 타입을 가져옴
        DailySuggestion suggestion = dailySuggestionRepository.findFirstByOrderByIdDesc();
        if (suggestion == null) {
            throw new RuntimeException("추천이 없습니다");
        }
        return suggestion.getOriginalQuery();
    }
    }




