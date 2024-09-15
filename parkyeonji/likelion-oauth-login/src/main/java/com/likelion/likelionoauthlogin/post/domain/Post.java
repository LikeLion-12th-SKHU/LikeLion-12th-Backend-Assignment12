package com.likelion.likelionoauthlogin.post.domain;

import com.likelion.likelionoauthlogin.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "POST_ID", nullable = false)
    private Long id;

    private String title;
    private String imageUrl;
    private String content;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @Builder
    public Post(String title, String imageUrl, String content, User user) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.content = content;
        this.user = user;
    }

}
