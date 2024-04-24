package com.example.demo.community.service;

import com.example.demo.community.dto.PostDto;
import com.example.demo.community.entity.Post;
import com.example.demo.community.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    // CREATE
    public PostDto createOOTD(String title, String content, String category, MultipartFile imgFile) throws IOException {
        Post post = new Post();
        String oriImgName = imgFile.getOriginalFilename();
        String imgName = "";

        String projectPath = System.getProperty("user.dir") + "/src/main/resources/static/files/";

        post.setTitle(title);
        post.setContent(content);
        post.setCategory(category);

        UUID uuid = UUID.randomUUID();

        String savedFileName = uuid + "_" + oriImgName;

        imgName = savedFileName;

        File saveFile = new File(projectPath, imgName);

        imgFile.transferTo(saveFile);

        post.setImgName(imgName);
        post.setImgPath("/files/" + imgName);

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
