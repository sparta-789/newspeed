package com.sparta.newspeed.dto;

import com.sparta.newspeed.entity.Post;
import lombok.Getter;
import java.time.LocalDateTime;


@Getter
public class PostResponseDto {    // 게시물 조회 요청에 대한 응답으로 사용
    private Long postId;
    private String title;
    private String author;
    private String contents;
    private LocalDateTime createdAt;
//    private LocalDateTime modifiedAt; 수정시간 - 상인님 코드

    public PostResponseDto(Long postId, String title, String author, String contents, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.postId = postId;
        this.title = title;
        this.author = author;
        this.contents = contents;
        this.createdAt = createdAt;
//        this.modifiedAt = modifiedAt;
    }
}
