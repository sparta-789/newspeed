package com.sparta.newspeed.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "post_liked_info")
public class PostLikedInfo extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_liked_id")
    private Long id;
    private Long postId;
    private String username;
    private Boolean isLiked;

    public PostLikedInfo() {

    }
    public PostLikedInfo(Long postId, String username) {
        this.postId = postId;
        this.username = username;
    }

    public Boolean getLiked() {
        return isLiked;
    }

    public void setLiked(Boolean liked) {
        isLiked = liked;
    }
}
