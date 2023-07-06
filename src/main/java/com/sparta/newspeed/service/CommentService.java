package com.sparta.newspeed.service;

import com.sparta.newspeed.dto.CommentRequestDto;
import com.sparta.newspeed.dto.CommentResponseDto;
import com.sparta.newspeed.dto.PostResponseDto;
import com.sparta.newspeed.entity.*;
import com.sparta.newspeed.repository.CommentLikeInfoRepository;
import com.sparta.newspeed.repository.CommentRepository;
import com.sparta.newspeed.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.sparta.newspeed.repository.PostRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private CommentLikeInfoRepository commentLikedInfoRepository;

    public void addLikeComment(Long commentId, UserDetailsImpl userDetails) {
        String username = userDetails.getUsername();
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));
        if (comment.getUser().getUsername().equals(username)) {
            throw new IllegalArgumentException("자신의 게시글에는 '좋아요'를 할 수 없습니다.");
        }

        CommentLikedInfo commentLikedInfo = commentLikedInfoRepository.findByCommentIdAndUsername(commentId, username).orElse(null);

        if (commentLikedInfo == null) {
            // 좋아요 요청이 처음일 경우, 새로운 LikedInfo 생성
            commentLikedInfo = new CommentLikedInfo(commentId, username);
            commentLikedInfo.setStatus("liked");
        } else {
            if (commentLikedInfo.getStatus().equals("canceled")) {
                // 좋아요가 취소된 상태에서 요청 시 status를 "liked"로 변경
                commentLikedInfo.setStatus("liked");
            } else {
                // 이미 좋아요를 누른 상태에서 요청 시 status를 "canceled"로 변경
                commentLikedInfo.setStatus("canceled");
            }
        }

        commentLikedInfoRepository.save(commentLikedInfo);

        updateCommentLikedCount(commentId);
    }

    private void updateCommentLikedCount(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다."));
        Integer commentLikedCount = commentLikedInfoRepository.countByCommentIdAndStatus(commentId, "liked");
        comment.setCommentLikedCount(commentLikedCount);
        commentRepository.save(comment);
    }
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

    public CommentResponseDto getCommentById(Long Id) {
        Comment comment = findComment(Id);

        return new CommentResponseDto(comment);
    }
    @Transactional
    public Comment findComment(Long id) {
        return commentRepository.findById(id).orElseThrow(
                ()-> new IllegalArgumentException("존재하지 않은 id 입니다. 다시 입력해 주세요")
        );
    }
}
