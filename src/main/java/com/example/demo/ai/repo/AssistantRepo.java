package com.example.demo.ai.repo;

import com.example.demo.ai.entity.AssistantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AssistantRepo extends JpaRepository<AssistantEntity, Long> {
  Optional<AssistantEntity> findAssistantByName(String assistantName);
  Optional<AssistantEntity> findAssistantByAssistantId(String assistantId);
  Optional<AssistantEntity> findAssistantByAssistantTypeAndVersion(String type, String version);
}
