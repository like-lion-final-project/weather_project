package com.example.demo.community.dto;

import com.example.demo.community.entity.Comment;
import com.example.demo.community.entity.Post;
import com.example.demo.user.entity.User;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {
    private Long id;
    @Setter
    private String title; // 제목
    @Setter
    private String content; // 내용
    @Setter
    private String category; // 카테고리
    @Setter
    private LocalDateTime createdDate; // 생성일
    @Setter
    private List<Comment> comments; // 댓글
    @Setter
    private String imgName; // 이미지 파일명
    @Setter
    private String imgPath; // 이미지 조회 경로
    @Setter
    private User userEntity;

    public static PostDto fromEntity(Post entity) {
        PostDtoBuilder builder = PostDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .content(entity.getContent())
                .category(entity.getCategory())
                .createdDate(entity.getCreatedDate())
                .comments(entity.getComments())
                .imgName(entity.getImgName())
                .imgPath(entity.getImgPath());
                //.userEntity(entity.getUserEntity());

        return builder.build();

    }
}
