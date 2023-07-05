package com.sparta.newspeed.repository;

import com.sparta.newspeed.entity.LikedInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikedInfoRepository extends JpaRepository<LikedInfo, Long> {
    Integer countByPostId(Long postId);
    Optional<LikedInfo> findByPostIdAndUsernameAndStatus(Long postId, String username, String status);

    Optional<LikedInfo> findByPostIdAndUsername(Long postId, String username);

    Integer countByPostIdAndStatus(Long postId, String status);

//    boolean existsByPostIdAndUsername(Long postId, String username);

//    void deleteByPostIdAndUsername(Long postId, String username);
}
