package com.likelion.oauth2test.post.domain;

import com.likelion.oauth2test.post.api.dto.request.PostUpdateReqDto;
import com.likelion.oauth2test.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post { // id title image content
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id", nullable = false)
    private Long id;

    private String title;
    private String image;
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Post(String title, String image, String content, User user) {
        this.title = title;
        this.image = image;
        this.content = content;
        this.user = user;
    }

    public void update(PostUpdateReqDto postUpdateReqDto) {
        this.title = postUpdateReqDto.title();
        this.content = postUpdateReqDto.content();
    }

    public void updateImage(String image) {
        this.image = image;
    }

}
