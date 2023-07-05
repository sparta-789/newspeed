package com.sparta.newspeed.repository;
import com.sparta.newspeed.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
//    Optional<Comment> findByPost_IdAndId(Long postId,Long commentId);
}
