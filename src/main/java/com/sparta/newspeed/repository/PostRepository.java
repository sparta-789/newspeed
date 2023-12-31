package com.sparta.newspeed.repository;

import com.sparta.newspeed.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByOrderByModifiedAtDesc(); //내림차순 정렬

    Post save(Post post);
}
