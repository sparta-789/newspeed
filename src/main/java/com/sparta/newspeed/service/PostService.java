package com.sparta.newspeed.service;

import com.sparta.newspeed.dto.PostListResponseDto;
import com.sparta.newspeed.dto.PostRequestDto;
import com.sparta.newspeed.dto.PostResponseDto;
import com.sparta.newspeed.entity.Post;
import com.sparta.newspeed.entity.User;
import com.sparta.newspeed.entity.UserRoleEnum;
import com.sparta.newspeed.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    public PostResponseDto createPost(PostRequestDto requestDto, User user) {
        Post post = new Post(requestDto);
        post.setUser(user);
        postRepository.save(post);

        return new PostResponseDto(post);
    }

    public PostListResponseDto getPosts() {
        List<PostResponseDto> postList = postRepository.findAll().stream()
                .map(PostResponseDto::new)
                .collect(Collectors.toList());

        return new PostListResponseDto(postList);
    }

    public PostResponseDto getPostById(Long id) {
        Post post = findPost(id);

        return new PostResponseDto(post);
    }

    public PostResponseDto updatePost(Long id, PostRequestDto requestDto, User user) {
        Post post = findPost(id);

        if(!user.getRole().equals(UserRoleEnum.ADMIN)&&!post.getUser().equals(user)) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }
        post.setTitle(requestDto.getTitle());
        post.setContent(requestDto.getContent());

        return new PostResponseDto(post);
    }

    public void deletePost(Long id, User user) {
    }

    private Post findPost(long id) {
        return postRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않는 게시글입니다.")
        );
    }
}
