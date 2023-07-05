package com.sparta.newspeed.dto;

import com.sparta.newspeed.entity.Comment;
import com.sparta.newspeed.entity.Post;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Getter
@Setter
public class PostResponseDto {
    private Long id;
    private String title;
    private String contents;
    private LocalDateTime createAt;
    private LocalDateTime modifiedAt;
    private String username;
    private List<CommentResponseDto> commentList=new ArrayList<>();

    public PostResponseDto(Post post) {
        this.id = post.getPostId();
        this.title = post.getTitle();
        this.contents = post.getContents();
        this.createAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();
        this.username = post.getUser().getUsername();
        this.commentList.addAll(post.getCommentList().stream().sorted(Comparator.comparing(Comment::getCreatedAt).reversed()).map(CommentResponseDto::new).toList());
        //생성일 내림차순 정렬, 추가될 땐 맨 앞에 추가되로록함.
    }


}
