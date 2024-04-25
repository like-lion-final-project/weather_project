package com.example.demo.cody.repo;

import com.example.demo.cody.entity.DailySuggestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailySuggestionRepository extends JpaRepository<DailySuggestion,Long> {

}
