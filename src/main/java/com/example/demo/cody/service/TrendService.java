package com.example.demo.cody.service;

import com.example.demo.cody.dto.CategoryFeedbackCountDto;
import com.example.demo.cody.dto.TrendDto;
import com.example.demo.cody.entity.SuggestionFeedback;
import com.example.demo.cody.repo.SuggestionFeedbackRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TrendService {

    private final SuggestionFeedbackRepository feedbackRepository;

    public List<CategoryFeedbackCountDto> countFeedbacksByCategory() {

        // 현재 시간을 기준으로 5분 전의 피대븍수 조회
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime fiveMinutesAgo = now.minusMinutes(5);

        List<SuggestionFeedback> feedbacks = feedbackRepository.findByCreateAtAfter(fiveMinutesAgo);

        Map<String, Long> feedbackCountsByCategory = feedbacks.stream()
                .collect(Collectors.groupingBy(
                        feedback -> (feedback.getCategory().getType()),
                        Collectors.counting()
                ));

        // CategoryFeedbackCountDto 리스트로 변환
        return feedbackCountsByCategory.entrySet().stream()
                .map(entry -> new CategoryFeedbackCountDto(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }


    public List<TrendDto> calculateRealTimeTrends() {
        // 현재 시간을 기준으로 5분 전의 별점 데이터를 조회
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime fiveMinutesAgo = now.minusMinutes(5);

        // 이전 시간 이후에 등록된 피드백들을 가져옴
        List<SuggestionFeedback> feedbacks = feedbackRepository.findByCreateAtAfter(fiveMinutesAgo);

        // 별점 데이터를 기반으로 트렌드를 계산
        Map<String, Double> categoryAverageRatings = feedbacks.stream()
                .collect(Collectors.groupingBy(
                        feedback -> feedback.getCategory().getType(),
                        Collectors.averagingInt(SuggestionFeedback::getRating)
                ));

        // 각 카테고리별 별점 평균을 TrendDto 리스트로 변환하여 반환
        return categoryAverageRatings.entrySet().stream()
                .map(entry -> new TrendDto(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }


}