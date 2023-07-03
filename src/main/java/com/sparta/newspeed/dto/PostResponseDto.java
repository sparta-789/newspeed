package com.sparta.newspeed.dto;

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

    public PostResponseDto(Long postId, String title, String author, String contents, LocalDateTime createdAt) {
        this.postId = this.postId;
        this.title = this.title;
        this.author = this.author;
        this.contents = this.contents;
        this.createdAt = this.createdAt;
//        this.modifiedAt = modifiedAt;
    }
}
