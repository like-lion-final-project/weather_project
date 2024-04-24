package com.example.demo.cody.repo;

import com.example.demo.cody.entity.SuggestionFeedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SuggestionFeedbackRepository extends JpaRepository<SuggestionFeedback,Long> {
    List<SuggestionFeedback> findByQuery(String query);
    List<SuggestionFeedback> findByCreateAtAfter(LocalDateTime time);
}
