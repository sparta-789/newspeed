package com.sparta.newspeed.service;

import com.sparta.newspeed.dto.CommentResponseDto;
import com.sparta.newspeed.entity.Comment;
import com.sparta.newspeed.entity.Post;
import com.sparta.newspeed.entity.User;
import com.sparta.newspeed.repository.CommentRepository;
import com.sparta.newspeed.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @Transactional
    public CommentResponseDto createComment(com.sparta.newspeed.dto.CommentRequestDto requestDto, User user) {
        //양방향일 때
        Post post = postRepository.findById(requestDto.getPostId()).orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        Comment comment = new Comment(post, requestDto.getContents(), user);
        post.addCommentList(comment);
        log.info("댓글 " + comment.getContents() + " 등록");
        return new CommentResponseDto(commentRepository.save(comment));
    }
}
