package com.sparta.newspeed.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "comment")
@NoArgsConstructor
public class Comment extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @Column(nullable = false)
    private String writer;

    @Column(nullable = false)
    private String contents;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_no")
    private Post post;

    public Comment(String writer, String contents, User user, Post post) {
        this.writer = user.getUsername();
        this.user = user;
        this.post = post;
        this.contents = contents;
    }

    public void update(String contents) {
        setContents(contents);
    }
}
