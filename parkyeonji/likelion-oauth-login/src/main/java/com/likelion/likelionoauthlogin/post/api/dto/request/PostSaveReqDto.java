package com.likelion.likelionoauthlogin.post.api.dto.request;

import com.likelion.likelionoauthlogin.post.domain.Post;

public record PostSaveReqDto(
        String title,
        String content
) {
}
