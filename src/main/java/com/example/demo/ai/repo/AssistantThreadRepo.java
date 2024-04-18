package com.example.demo.ai.repo;

import com.example.demo.ai.entity.AssistantThread;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssistantThreadRepo extends JpaRepository<AssistantThread, Long> {
}
