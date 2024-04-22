package com.example.demo.ai.repo;

import com.example.demo.ai.entity.AssistantThreadMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssistantThreadMessageRepo extends JpaRepository<AssistantThreadMessage,Long> {
}
