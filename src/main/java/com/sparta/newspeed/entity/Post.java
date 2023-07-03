package com.sparta.newspeed.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table
@NoArgsConstructor
public class Post extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //PK값 자동 생성
    private Long id; // 게시글 고유 id
    @Column (nullable = false, length = 500)
    private String title; // 제목
    @Column (nullable = false, length = 10)
    private String username; // 작성자명
    @Column (nullable = false, length = 1000)
    private String contents; // 작성내용

    public Post(String title, String username, String contents) {
        this.title = title;
        this.username = username;
        this.contents = contents;
    }
}
