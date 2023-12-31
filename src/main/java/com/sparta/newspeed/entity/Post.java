package com.sparta.newspeed.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "post")
@NoArgsConstructor
public class Post extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //PK값 자동 생성
    @Column(name = "post_id")
    private Long postId; // 게시글 고유 id
    @Column(nullable = false, length = 500)
    private String title; // 제목
    @Column(nullable = false, length = 10)
    private String author; // 작성자명
    @Column(nullable = false, length = 1000)
    private String contents; // 작성내용
    @Column(nullable = false)
    private Integer postLikedCount;

    //setAuthor
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "post")
    private List<Comment> commentList = new ArrayList<>();

    public Post(String title, User user, String contents) {
        this.title = title;
        this.author = user.getUsername();
        this.contents = contents;
        this.user = user;
        this.postLikedCount = 0; // 기본값 설정
    }
    public Integer getPostLikedCount() {
        return postLikedCount;
    }
    public void setPostLikedCount(Integer postLikedCount) {
        this.postLikedCount = postLikedCount;
    }

    public void addCommentList(Comment comment) {
        this.commentList.add(0,comment);
    }
}