package com.example.demo.cody.service;

import com.example.demo.cody.dto.FeedbackDto;
import com.example.demo.cody.dto.ItemDto;
import com.example.demo.cody.entity.ClothsCategory;
import com.example.demo.cody.entity.SuggestionFeedback;
import com.example.demo.cody.repo.ClothsCategoryRepository;
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

        // 피드백의 카테고리를 찾습니다.
        Optional<ClothsCategory> optionalCategory = clothsCategoryRepository.findByType(feedbackDto.getCategory());
        ClothsCategory category = optionalCategory.orElseGet(() -> {
            feedbackDto.setCategory("기타");
            return clothsCategoryRepository.findByType("기타").orElse(null);
        });
// 피드백 엔티티를 생성하여 저장합니다.
        SuggestionFeedback suggestionFeedback = SuggestionFeedback.builder()
                .rating(feedbackDto.getRating())
                .image(feedbackDto.getImage())
                .query(feedbackDto.getQuery())
                .category(category)
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .build();

        suggetionFeedbackRepository.save(suggestionFeedback);
        }
    }




