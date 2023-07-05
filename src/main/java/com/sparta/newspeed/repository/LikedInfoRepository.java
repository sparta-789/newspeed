package com.sparta.newspeed.repository;

import com.sparta.newspeed.entity.LikedInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikedInfoRepository extends JpaRepository<LikedInfo, Long> {
    Integer countByPostId(Long postId);
    boolean existsByPostIdAndUsername(Long postId, String username);

    void deleteByPostIdAndUsername(Long postId, String username);
}
