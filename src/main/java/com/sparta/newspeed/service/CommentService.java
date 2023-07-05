package com.sparta.newspeed.service;

import com.sparta.newspeed.dto.CommentRequestDto;
import com.sparta.newspeed.dto.CommentResponseDto;
import com.sparta.newspeed.entity.Comment;
import com.sparta.newspeed.entity.User;
import com.sparta.newspeed.entity.UserRoleEnum;
import com.sparta.newspeed.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.sparta.newspeed.entity.Post;
import com.sparta.newspeed.repository.PostRepository;
import lombok.extern.slf4j.Slf4j;

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

    @Transactional
    public void deleteComment(User user, CommentRequestDto requestDto) {
        Comment comment = findComment(requestDto.getCommentId());

        if (!user.getRole().equals(UserRoleEnum.ADMIN)&&!comment.getUser().equals(user)) {
            throw new SecurityException("삭제 권한이 없습니다.");
        }

        commentRepository.delete(comment);
    }

    @Transactional
    public CommentResponseDto updateComment(User user, CommentRequestDto requestDto) {

        Comment comment = findComment(requestDto.getCommentId());

        if (!user.getRole().equals(UserRoleEnum.ADMIN)&&!comment.getUser().equals(user)) {
            throw new SecurityException("수정 권한이 없습니다.");
        }

        comment.update(requestDto.getContents());

        return new CommentResponseDto(comment);
    }


    @Transactional
    public Comment findComment(Long id) {
        return commentRepository.findById(id).orElseThrow(
                ()-> new IllegalArgumentException("존재하지 않은 id 입니다. 다시 입력해 주세요")
        );
    }
}
