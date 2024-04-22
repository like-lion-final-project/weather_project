package com.example.demo.ai.repo;

import com.example.demo.ai.entity.Assistant;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface AssistantRepo extends JpaRepository<Assistant, Long> {
  Optional<Assistant> findAssistantByName(String assistantName);
  Optional<Assistant> findAssistantByAssistantId(String assistantId);
}
