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
    private LocalDateTime modifiedAt;

    public CommentResponseDto(Comment comment) {
        this.contents=comment.getContents();
        this.username=comment.getUsername();
        this.createdAt=comment.getCreatedAt();
        this.modifiedAt = comment.getModifiedAt();
    }
}