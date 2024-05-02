package com.example.demo.community.repository;

import com.example.demo.community.entity.Comment;
import com.example.demo.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByUserEntity(User userEntity, Pageable page);

    List<Comment> findAllByUserEntity(User userEntity);
}
