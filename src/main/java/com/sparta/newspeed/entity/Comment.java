package com.sparta.newspeed.entity;

import com.sparta.newspeed.entity.Post;
import com.sparta.newspeed.entity.Timestamped;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Table(name = "comment")
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class Comment extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "comment_contents", nullable = false)
    private String contents;

    @Column(name = "comment_username", nullable = false, length = 12)
    private String username;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false, updatable = false)
    public Post post;

    public Comment(Post post, String contents, User user) {
        this.post = post;
        this.contents = contents;
        this.username = user.getUsername();
        this.user = user;
    }

    public void update(String contents) {
        setContents(contents);
    }
}