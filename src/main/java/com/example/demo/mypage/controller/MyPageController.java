package com.example.demo.mypage.controller;

import com.example.demo.mypage.dto.MyPageDto;
import com.example.demo.mypage.service.MyPageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class MyPageController {
    private final MyPageService myPageService;

    @GetMapping("/mypage")
    public String myPost(
            @RequestParam("key") String token,
            @RequestParam(value = "postPage", defaultValue = "1") Integer postPage,
            @RequestParam(value = "commentPage", defaultValue = "1") Integer commentPage,
            @RequestParam(value = "recentViewPage", defaultValue = "1") Integer recentViewPage,
            Model model
    ) {
        MyPageDto myPageDto = myPageService.readMyPage(token, postPage, commentPage, recentViewPage);
        model.addAttribute("posts", myPageDto.getPostDtoList());
        model.addAttribute("postTotalPage", myPageDto.getPostTotalPages());
        model.addAttribute("postCurrentPage", myPageDto.getPostCurrentPage());
        model.addAttribute("comments", myPageDto.getCommentDtoList());
        model.addAttribute("commentTotalPage", myPageDto.getCommentTotalPages());
        model.addAttribute("commentCurrentPage", myPageDto.getCommentCurrentPage());
        model.addAttribute("recentPosts", myPageDto.getRecentPostList());
        model.addAttribute("recentTotalPage", myPageDto.getRecentTotalPages());
        model.addAttribute("recentCurrentPage", myPageDto.getRecentCurrentPage());
        return "mypage";
    }
}
