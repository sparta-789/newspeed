package com.sparta.newspeed.repository;

import com.sparta.newspeed.entity.CommentLikedInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface CommentLikeInfoRepository extends JpaRepository<CommentLikedInfo, Long> {
    Integer countByCommentId(Long commentId);
    Optional<CommentLikedInfo> findByCommentIdAndUsernameAndStatus(Long commentId, String username, String status);

    Optional<CommentLikedInfo> findByCommentIdAndUsername(Long commentId, String username);

    Integer countByCommentIdAndStatus(Long commentId, String status);
}
