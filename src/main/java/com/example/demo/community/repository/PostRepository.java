package com.example.demo.community.repository;

import com.example.demo.community.dto.PostDto;
import com.example.demo.community.entity.Post;
import com.example.demo.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByOrderByCreatedDateDesc();

    List<Post> findAllByUserEntity(User user, Pageable page);

    List<Post> findAllByUserEntity(User user);

    List<Post> findAllByIdIn(List<Long> ids, Pageable page);
}
