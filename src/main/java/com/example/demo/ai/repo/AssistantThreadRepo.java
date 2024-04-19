package com.example.demo.ai.repo;

import com.example.demo.ai.entity.Assistant;
import com.example.demo.ai.entity.AssistantThread;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AssistantThreadRepo extends JpaRepository<AssistantThread, Long> {
    Optional<AssistantThread> findThreadByThreadId(String threadId);
    Optional<AssistantThread> findThreadByUserId(Long userId);

}
