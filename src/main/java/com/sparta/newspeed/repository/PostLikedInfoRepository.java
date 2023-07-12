package com.sparta.newspeed.repository;

import com.sparta.newspeed.entity.PostLikedInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikedInfoRepository extends JpaRepository<PostLikedInfo, Long> {
    Integer countByPostId(Long postId);
    //Optional<PostLikedInfo> findByPostIdAndUsernameAndStatus(Long postId, String username, String status);

    Optional<PostLikedInfo> findByPostIdAndUsername(Long postId, String username);

    //Integer countByPostIdAndStatus(Long postId, String status);
    Integer countByPostIdAndIsLikedIsTrue(Long postId);

//    boolean existsByPostIdAndUsername(Long postId, String username);

//    void deleteByPostIdAndUsername(Long postId, String username);
}
