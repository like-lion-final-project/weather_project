package com.example.demo.mypage.service;

import com.example.demo.community.dto.CommentDto;
import com.example.demo.community.dto.PostDto;
import com.example.demo.community.entity.Post;
import com.example.demo.community.repository.RecentViewPostRepository;
import com.example.demo.community.service.CommentService;
import com.example.demo.community.service.PostService;
import com.example.demo.jwt.JwtTokenUtils;
import com.example.demo.mypage.dto.MyPageDto;
import com.example.demo.user.entity.CustomUserDetails;
import com.example.demo.user.entity.User;
import com.example.demo.user.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MyPageService {
    private final UserRepo userRepo;
    private final PostService postService;
    private final CommentService commentService;
    private final RecentViewPostRepository recentViewPostRepository;
    private final JwtTokenUtils jwtTokenUtils;

    public MyPageDto readMyPage(String token, Integer postPage, Integer commentPage, Integer recentViewPage) {
        log.info("token: " + token);
        String username = jwtTokenUtils
                .parseClaimsJws(token)
                .getSubject();

        log.info("유저이름: " + username);
        User user = userRepo.findUserByUsername(username).orElseThrow();

        Integer offset = 0;

        if (postPage > 1) {
            offset = postPage - 1;
        }

        List<PostDto> postDtoList = postService.readAllMyPost(user, offset);

        if (commentPage > 1) {
            offset = commentPage - 1;
        }

        List<CommentDto> commentDtoList = commentService.readAllMyComment(user, offset);

        if (recentViewPage > 1) {
            offset = recentViewPage - 1;
        }

        List<Long> postIdList = recentViewPostRepository.findPostIdsByUserIdAndViewDateAfter(user.getId(), LocalDateTime.now().minusHours(24));
        List<PostDto> recentPostList = postService.readRecentPost(postIdList, offset);

        return MyPageDto.builder()
                .postDtoList(postDtoList)
                .postTotalPages((int) Math.ceil((double) postService.getMyPostListSize(user) / 4))
                .postCurrentPage(postPage)
                .commentDtoList(commentDtoList)
                .commentTotalPages((int) Math.ceil((double) commentService.getMyCommentListSize(user) / 4))
                .commentCurrentPage(commentPage)
                .recentPostList(recentPostList)
                .recentTotalPages((int) Math.ceil((double) postIdList.size() == 0 ? 1 : postIdList.size() / 4))
                .recentCurrentPage(recentViewPage)
                .build();
    }
}
