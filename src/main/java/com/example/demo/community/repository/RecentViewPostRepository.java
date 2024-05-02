package com.example.demo.community.repository;

import com.example.demo.community.entity.RecentViewPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface RecentViewPostRepository extends JpaRepository<RecentViewPost, Long> {
    @Query("SELECT r.postId FROM RecentViewPost r WHERE r.userId = :userId AND r.viewDate > :dateTime")
    List<Long> findPostIdsByUserIdAndViewDateAfter(@Param("userId") Long userId, @Param("dateTime") LocalDateTime dateTime);
}
