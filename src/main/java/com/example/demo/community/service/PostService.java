package com.example.demo.community.service;

import com.example.demo.community.dto.PostDto;
import com.example.demo.community.entity.Post;
import com.example.demo.community.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    // CREATE
    public PostDto createOOTD(PostDto postDto) {
        Post post = new Post();

        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setCategory(postDto.getCategory());

        return PostDto.fromEntity(postRepository.save(post));

    }
    // READ
    // READ All
    public List<PostDto> readAll() {
        List<PostDto> postDtos = new ArrayList<>();
        for(Post post: postRepository.findAllByOrderByCreatedDateDesc()) {
            postDtos.add(PostDto.fromEntity(post));
        }
        return postDtos;
    }

    // READ One
    public PostDto readOne(Long postId) {
        return PostDto.fromEntity(postRepository.findById(postId).orElseThrow());
    }
    // UPDATE
    public void updateOOTD(PostDto postDto, Long postId) {
        Post post = postRepository.findById(postId).orElseThrow();

        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setCategory(postDto.getCategory());

        PostDto.fromEntity(postRepository.save(post));

    }
    // DELETE
    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow();
        postRepository.delete(post);
    }
}
