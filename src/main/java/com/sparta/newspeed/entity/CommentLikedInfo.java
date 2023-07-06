package com.sparta.newspeed.entity;

import jakarta.persistence.*;


@Entity
@Table(name = "comment_liked_info")
public class CommentLikedInfo extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_liked_id")
    private Long id;
    private Long commentId;
    private String username;
    private String status;

    public CommentLikedInfo() {

    }
    public CommentLikedInfo(Long commentId, String username) {
        this.commentId = commentId;
        this.username = username;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
