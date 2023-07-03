package com.sparta.newspeed.entity;

import com.sparta.newspeed.dto.PostRequestDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table
@NoArgsConstructor
public class Post extends Timestamped{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //PK값 자동 생성
    @Column(name = "post_id")
    private Long postId; // 게시글 고유 id
    @Column (nullable = false, length = 500)
    private String title; // 제목
    @Column (nullable = false, length = 10)
    private String author; // 작성자명
    @Column (nullable = false, length = 1000)
    private String contents; // 작성내용


    //setAuthor
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User username;


    public Post(String title, String author, String contents) {
        this.title = title;
        this.author = author;
        this.contents = contents;

    }


}
