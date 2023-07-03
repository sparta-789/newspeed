package com.sparta.newspeed.dto;

import com.sparta.newspeed.entity.Post;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostResponseDto {    // 게시물 조회 요청에 대한 응답으로 사용
    private String title;
    private String username;
    private String contents;
    private LocalDateTime createdAt;
    private LocalDateTime mpdifieAt;

    public PostResponseDto(Post post){
        this.title = post.getTitle();
        this.username = post.getUsername();
        this.contents = post.getContents();
        this.createdAt = post.getCreateAt();
        this.mpdifieAt = post.getModifiedAt();
    }
}
