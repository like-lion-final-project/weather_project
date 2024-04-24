package com.example.demo.community.dto;

import com.example.demo.community.entity.Comment;
import com.example.demo.community.entity.Post;
import com.example.demo.user.entity.User;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private Long id;
    @Setter
    private String content;
    @Setter
    private LocalDateTime createdAt;
    @Setter
    private Post post;
    @Setter
    private User userEntity;

    public static CommentDto fromEntity(Comment entity) {
        CommentDto.CommentDtoBuilder builder = CommentDto.builder()
                .id(entity.getId())
                .content(entity.getContent())
                .createdAt(entity.getCreatedAt())
                .post(entity.getPost())
                .userEntity(entity.getUserEntity());

        return  builder.build();
    }
}
