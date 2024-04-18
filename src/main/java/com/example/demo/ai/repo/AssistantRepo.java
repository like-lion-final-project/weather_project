package com.example.demo.ai.repo;

import com.example.demo.ai.entity.Assistant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AssistantRepo extends JpaRepository<Assistant, Long> {
  Optional<Assistant> findAssistantByName(String name);
}
