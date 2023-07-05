package com.sparta.newspeed.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentRequestDto {
    private Long commentId;
    private String contents;

    public CommentRequestDto(Long commentId, String contents) {
        this.commentId = commentId;
        this.contents = contents;
    }
}
