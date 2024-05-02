package com.example.demo.community.controller;

import com.example.demo.cody.entity.ClothsCategory;
import com.example.demo.cody.repo.ClothsCategoryRepository;
import com.example.demo.community.dto.PostDto;
import com.example.demo.community.entity.Post;
import com.example.demo.community.service.CommentService;
import com.example.demo.community.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("ootd")
public class CommunityController {
    private final PostService postService;
    private final CommentService commentService;
    private final ClothsCategoryRepository clothsCategoryRepository;

    @GetMapping("")
    public String ootdPage(Model model) {
        model.addAttribute("posts", postService.readAll());
        return "ootd";
    }
    @GetMapping("/write")
    public String ootdWritePage(Model model) {
        List<ClothsCategory> category = new ArrayList<>();
        category = clothsCategoryRepository.findAll();
        model.addAttribute("category", category);
        return "ootdWrite";
    }

    @PostMapping("/write")
    public String createOOTD(
            @RequestParam("title")
            String title,
            @RequestParam("content")
            String content,
            @RequestParam("category")
            String category,
            MultipartFile imgFile,
            Model model,
            RedirectAttributes redirectAttributes
    ) throws IOException
    {
        PostDto dto =postService.createOOTD(title, content, category, imgFile);

        if (dto == null) {
            redirectAttributes.addFlashAttribute("message",
                    "새 글을 작성할 수 없습니다.");
        }

        return "redirect:/ootd";
    }

    @GetMapping("/view/{postId}")
    public String viewPost(
            @PathVariable("postId")
            Long postId,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        PostDto postDto = postService.readOne(postId);
        Long postUserId = postDto.getId();
        // postId 게시글 하나의 정보
        model.addAttribute("posts", postDto);
        System.out.println(postId);
        return "/ootdDetail";
    }

    @GetMapping("/update/{postId}")
    public String updatePostForm(
            @PathVariable("postId")
            Long postId,
            Model model
    ) {
        PostDto postDto = postService.readOne(postId);
        model.addAttribute("postForm", postDto);

        return "/ootdUpdate";
    }

    @PostMapping("/update/{postId}")
    public String updatePost(
            @PathVariable("postId")
            Long postId,
            PostDto postDto,
            Model model
    ) {

        model.addAttribute("message", "수정 완료");
        postService.updateOOTD(postDto, postId);
        return "redirect:/ootd";
    }

    @PostMapping("/delete/{postId}")
    public String deletePost(
            @PathVariable("postId")
            Long postId
    ) {
        postService.deletePost(postId);
        return "redirect:/ootd";
    }





}
