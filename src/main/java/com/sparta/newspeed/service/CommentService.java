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

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

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
