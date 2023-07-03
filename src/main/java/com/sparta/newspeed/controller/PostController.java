package com.sparta.newspeed.controller;

import com.sparta.newspeed.dto.*;
import com.sparta.newspeed.service.PostService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PostController {
    private PostService postService;

    //게시글 작성 API
    @PostMapping("/posts")
    public PostResponseDto createPost(@RequestBody PostRequestDto requestDto) {
        return postService.createPost(requestDto);
    }

    //선택한 게시글 삭제 API
    @DeleteMapping("/posts/{id}")
    public PostDeleteDto deletePost(@PathVariable Long id) {
        return postService.deletePost(id);
    }
}
