package com.example.demo.cody.repo;

import com.example.demo.cody.entity.DailySuggestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailySuggestionRepository extends JpaRepository<DailySuggestion,Long> {

    // 마지막 추천의 카테고리를 가져오는 메서드
    DailySuggestion findFirstByOrderByIdDesc();

}
