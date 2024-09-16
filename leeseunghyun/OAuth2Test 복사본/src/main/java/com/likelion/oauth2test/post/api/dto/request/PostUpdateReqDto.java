package com.likelion.oauth2test.post.api.dto.request;

import com.likelion.oauth2test.post.domain.Post;
import com.likelion.oauth2test.user.domain.Users;

public record PostUpdateReqDto(
        String title,
        String content
) {
    // DTO를 Post 엔티티로 변환하는 메서드
    public Post toEntity(Users user, String image) {
        return Post.builder()
                .title(title)      // 제목 설정
                .content(content)  // 내용 설정
                .image(image)      // 이미지 URL 설정
                .user(user)        // 작성자 설정
                .build();
    }
}
