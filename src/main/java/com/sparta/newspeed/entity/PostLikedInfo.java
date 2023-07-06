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
    private String status;

    public PostLikedInfo() {

    }
    public PostLikedInfo(Long postId, String username) {
        this.postId = postId;
        this.username = username;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
