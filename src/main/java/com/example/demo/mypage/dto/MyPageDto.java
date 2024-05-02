package com.example.demo.mypage.dto;

import com.example.demo.community.dto.CommentDto;
import com.example.demo.community.dto.PostDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MyPageDto {
    private List<PostDto> postDtoList;
    private List<CommentDto> commentDtoList;
    private List<PostDto> recentPostList;
    private Integer postTotalPages;
    private Integer postCurrentPage;
    private Integer commentTotalPages;
    private Integer commentCurrentPage;
    private Integer recentTotalPages;
    private Integer recentCurrentPage;
}
