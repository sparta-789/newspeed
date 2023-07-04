package com.sparta.newspeed.repository;

import com.sparta.newspeed.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
