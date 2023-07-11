package com.sparta.newspeed.service;

import com.sparta.newspeed.dto.PostListResponseDto;
import com.sparta.newspeed.dto.PostRequestDto;
import com.sparta.newspeed.dto.PostResponseDto;
import com.sparta.newspeed.entity.PostLikedInfo;
import com.sparta.newspeed.entity.Post;
import com.sparta.newspeed.entity.User;
import com.sparta.newspeed.entity.UserRoleEnum;
import com.sparta.newspeed.jwt.JwtUtil;
import com.sparta.newspeed.repository.PostLikedInfoRepository;
import com.sparta.newspeed.repository.PostRepository;
import com.sparta.newspeed.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final PostLikedInfoRepository postLikedInfoRepository;

    private final JwtUtil jwtUtil;

    //게시글 작성 API

    @Transactional
    public PostResponseDto createdPost(PostRequestDto requestDto, UserDetailsImpl userDetails){
        // JWT 토큰 검증
//        if (!jwtUtil.validateToken(requestDto.getToken())) {
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다.");
//        }
        User user = userDetails.getUser();
//        String username = jwtUtil.getUserInfoFromToken(requestDto.getToken()).getSubject();
//        if (!username.equals(currentUser.getUsername())) {
//            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "게시글 작성 권한이 없습니다.");
//        }
        Post post = new Post(requestDto.getTitle(), user, requestDto.getContents());
        return new PostResponseDto(postRepository.save(post));
    }


    public PostListResponseDto getPosts() {
        List<PostResponseDto> postList = postRepository.findAll().stream()
                .map(PostResponseDto::new)
                .collect(Collectors.toList());

        return new PostListResponseDto(postList);
    }

    public PostResponseDto getPostById(Long Id) {
        Post post = findPost(Id);

        return new PostResponseDto(post);
    }

    public PostResponseDto updatePost(Long id, PostRequestDto requestDto, User user) {
        Post post = findPost(id);

        if(!user.getRole().equals(UserRoleEnum.ADMIN)&&!post.getUser().equals(user)) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }
        post.setTitle(requestDto.getTitle());
        post.setContents(requestDto.getContents());

        return new PostResponseDto(post);
    }

    // post 좋아요
    @Transactional
    public void addLikePost(Long postId, UserDetailsImpl userDetails) {
        String username = userDetails.getUsername();
        // postId와 username을 이용해서 사용자가 이미 Like를 눌렀는지 확인
//        boolean alreadyLiked = likedInfoRepository.findByPostIdAndUsernameAndStatus(postId, username, "liked").isPresent();

        //자신의 게시글에 좋아요 X
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));
        if (post.getUser().getUsername().equals(username)) {
            throw new IllegalArgumentException("자신의 게시글에는 '좋아요'를 할 수 없습니다.");
        }
        PostLikedInfo postLikedInfo = postLikedInfoRepository.findByPostIdAndUsername(postId, username).orElse(null);

/*        if (postLikedInfo == null) {
            // 좋아요 요청이 처음일 경우, 새로운 LikedInfo 생성
            postLikedInfo = new PostLikedInfo(postId, username);
            postLikedInfo.setStatus("liked");
        } else {
            if (postLikedInfo.getStatus().equals("canceled")) {
                // 좋아요가 취소된 상태에서 요청 시 status를 "liked"로 변경
                postLikedInfo.setStatus("liked");
            } else {
                // 이미 좋아요를 누른 상태에서 요청 시 status를 "canceled"로 변경
                postLikedInfo.setStatus("canceled");
            }
        }*/
        if(postLikedInfo==null){
            //처음이면 좋아요 만들고 상태 true로 변경
            postLikedInfo = new PostLikedInfo(postId, username);
            postLikedInfo.setLiked(true);
        }else{
            //기존 상태의 반대로 전환
            postLikedInfo.setLiked(!postLikedInfo.getLiked());
        }

        postLikedInfoRepository.save(postLikedInfo);

        updatePostLikedCount(postId);
    }

    // count한 like 저장해주기
    private void updatePostLikedCount(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));
        Integer postLikedCount = postLikedInfoRepository.countByPostIdAndIsLikedIsTrue(postId);
        post.setPostLikedCount(postLikedCount);
        // TODO save가 꼭 있어야 하나?
//        postRepository.save(post);
    }

    //게시글 삭제 API
    @Transactional
    public String deletePost(Long id, UserDetailsImpl userDetails) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."));

        User currentUser = userDetails.getUser();
        if (!post.getAuthor().equals(currentUser.getUsername())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "작성자만 게시글을 삭제할 수 있습니다.");
        }

        postRepository.delete(post);

        return "게시글이 성공적으로 삭제되었습니다.";
    }

    private Post findPost(long id) {
        return postRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않는 게시글입니다.")
        );
    }
}
