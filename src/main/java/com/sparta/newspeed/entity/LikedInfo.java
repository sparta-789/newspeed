package com.sparta.newspeed.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "liked_info")
public class LikedInfo extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "liked_id")
    private Long id;
    private Long postId;
    private String username;
    private String status;

    public LikedInfo() {

    }
    public LikedInfo(Long postId, String username) {
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
