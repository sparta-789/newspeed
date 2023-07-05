package com.sparta.newspeed.controller;

import com.sparta.newspeed.dto.CommentResponseDto;
import com.sparta.newspeed.security.UserDetailsImpl;
import com.sparta.newspeed.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {
    private final CommentService commentService;

    //댓글 등록
    @PostMapping("/comments")
    public ResponseEntity<CommentResponseDto> createComment(@RequestBody com.sparta.newspeed.dto.CommentRequestDto commentRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.ok().body(commentService.createComment(commentRequestDto,userDetails.getUser()));
    }

    @DeleteMapping("/comments")
    public ResponseEntity<ApiResponseDto> deleteComment(@AuthenticationPrincipal UserDetailsImpl details, @RequestBody CommentRequestDto requestDto) {
        try {
            commentService.deleteComment(details.getUser(), requestDto);
            return ResponseEntity.status(204).body(new ApiResponseDto("삭제 되었습니다.", HttpStatus.NO_CONTENT.value()));
        } catch (SecurityException e) {
            return ResponseEntity.badRequest().body(new ApiResponseDto("삭제 권한이 없습니다.", HttpStatus.BAD_REQUEST.value()));
        }
    }

    @PutMapping("/comments")
    public ResponseEntity<CommentResponseDto> updateComment(@AuthenticationPrincipal UserDetailsImpl details, @RequestBody CommentRequestDto requestDto) {
        try {
            CommentResponseDto responseDto = commentService.updateComment(details.getUser(), requestDto);
            return ResponseEntity.ok().body(responseDto);
        } catch (SecurityException e) {
            throw new SecurityException("수정 권한이 없습니다.");
        }
    }


}
