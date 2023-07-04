package com.sparta.newspeed.dto;

import com.sparta.newspeed.entity.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CommentResponseDto {
    private String contents;
    private String username;
    private LocalDateTime createdAt;

    public CommentResponseDto(Comment comment) {
        this.contents=comment.getContents();
        this.username=comment.getUsername();
        this.createdAt=comment.getCreatedAt();
    }
}