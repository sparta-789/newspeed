package com.sparta.newspeed.service;

import com.sparta.newspeed.dto.PostRequestDto;
import com.sparta.newspeed.dto.PostResponseDto;
import com.sparta.newspeed.entity.Post;
import com.sparta.newspeed.entity.Timestamped;
import com.sparta.newspeed.entity.User;
import com.sparta.newspeed.jwt.JwtUtil;
import com.sparta.newspeed.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor //생성자 주입으로 데이터베이스에 대한 생성자를 생성하지 않아도 됨
public class PostService {
    private final PostRepository postRepository;
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final Timestamped timestamped;

    //게시글 작성 API
    @Transactional
    public PostResponseDto createdPost(PostRequestDto requestDto){
        User currentUser  = userService.getCurrentUser();// 현재 로그인한 사용자 정보 가져오기
        //JWT 토큰 검증
        if (!jwtUtil.validateToken(requestDto.getToken())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다.");
        }

        //사용자 인증
        String username = jwtUtil.getUserInfoFromToken(requestDto.getToken()).getSubject();
        if (!username.equals(currentUser.getUsername())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "게시글 작성 권한이 없습니다.");
        }

        Post post = new Post(requestDto.getTitle(), currentUser.getUsername(), requestDto.getContents());

        Post savedPost = postRepository.save(post);

        return new PostResponseDto(savedPost.getPostId(), savedPost.getTitle(), savedPost.getAuthor(), savedPost.getContents(), savedPost.getCreatedAt());



    }
    //게시글 삭제 API
    @Transactional
    public String deletePost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() ->  new ResponseStatusException(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."));

        User currentUser = userService.getCurrentUser();
        if (!post.getAuthor().equals(currentUser.getUsername())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "작성자만 게시글을 삭제할 수 있습니다.");
        }

        postRepository.delete(post);

        return "게시글이 성공적으로 삭제되었습니다.";
    }
}