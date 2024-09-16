package com.likelion.oauth2test.post.api.dto.request;

import com.likelion.oauth2test.post.domain.Post;
import com.likelion.oauth2test.user.domain.User;

public record PostRequestDto(
        String title,
        String content
) {
    public Post createPost(User user, String imgUrl) {
        return Post.builder()
                .title(title)
                .content(content)
                .imgUrl(imgUrl)
                .user(user)
                .build();
    }
}