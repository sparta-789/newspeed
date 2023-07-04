package com.sparta.newspeed.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentRequestDto {
    private Long postId;
    private Long commentId;
    private String contents;
}