package com.likelion.oauth2test.post.domain;

import com.likelion.oauth2test.post.api.dto.request.PostUpdateReqDto;
import com.likelion.oauth2test.user.domain.Users;
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
    @Column(name = "post_id", nullable = false)
    private Long id;

    private String title;
    private String image;
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    // Builder를 통한 생성자
    @Builder
    public Post(String title, String image, String content, Users user) {
        this.title = title;
        this.image = image;
        this.content = content;
        this.user = user;
    }

    // 게시글 수정
    public void update(PostUpdateReqDto postUpdateRequest) {
        this.title = postUpdateRequest.title();
        this.content = postUpdateRequest.content();
    }

    // 이미지 수정
    public void updateImage(String image) {
        this.image = image;
    }
}
