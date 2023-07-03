package com.sparta.newspeed.controller;

import com.sparta.newspeed.dto.PostRequestDto;
import com.sparta.newspeed.dto.PostResponseDto;
import com.sparta.newspeed.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // 게시글 작성 API
    @PostMapping("/posts")
    public ResponseEntity<PostResponseDto> createdPost(@RequestBody PostRequestDto requestDto) {
        PostResponseDto createdPost = postService.createdPost(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
    }

    // 선택한 게시글 삭제 API
    @DeleteMapping("/posts/{id}")
    public ResponseEntity<String> deletePost(@PathVariable Long id) {
        String message = postService.deletePost(id);
        return ResponseEntity.ok(message);
    }
}
