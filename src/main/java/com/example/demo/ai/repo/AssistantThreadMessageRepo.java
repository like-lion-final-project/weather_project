package com.example.demo.ai.repo;

import com.example.demo.ai.entity.AssistantThreadMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AssistantThreadMessageRepo extends JpaRepository<AssistantThreadMessage,Long> {
    @Query("SELECT atm FROM AssistantThreadMessage atm WHERE atm.assistantThread.user.id = :userId AND atm.isDeleteFromOpenAi = false ORDER BY atm.createdAt DESC")
    Optional<AssistantThreadMessage> findFirstByUserIdAndIsDeleteFromOpenAiFalseOrderByCreatedAtDesc(@Param("userId") Long userId);

}
