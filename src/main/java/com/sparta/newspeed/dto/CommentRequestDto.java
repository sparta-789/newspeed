package com.sparta.newspeed.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentRequestDto {
    private Long postId;
    private Long commentId;
    // 생성할 땐, 없어도 자동으로 DB에 저장이 되지만 수정 삭제시엔 필요함.
    // @JsonInclude(JsonInclude.Include.NON_NULL) 어노테이션 덕에
    // 생성시엔 넣지 않아도 자동으로 db에 c_id 가 부여되는 걸 확인하였음
    private String contents;
}