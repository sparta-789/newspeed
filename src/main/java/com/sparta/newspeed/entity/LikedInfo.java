package com.sparta.newspeed.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "liked_info")
public class LikedInfo {
    // TODO liked_id가 계속 올라가야 하나?
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "liked_id")
    private Long id;
    private Long postId;
    private String username;

    public LikedInfo() {

    }
    public LikedInfo(Long postId, String username) {
        this.postId = postId;
        this.username = username;
    }
}
