package com.example.demo.community.service;

import com.example.demo.community.dto.CommentDto;
import com.example.demo.community.repository.CommentRepository;
import com.example.demo.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    public List<CommentDto> readAllMyComment(User user, Integer offset) {
        Pageable page = PageRequest.of(offset, 4);

        return commentRepository.findAllByUserEntity(user, page)
                .stream()
                .map(CommentDto::fromEntity)
                .collect(Collectors.toList());
    }

    public Integer getMyCommentListSize(User user) {
        Integer listSize = commentRepository.findAllByUserEntity(user).size();
        return listSize == 0 ? 1 : listSize;
    }
}
