package com.likelion.oauth2test.post.api.dto.response;

import com.likelion.oauth2test.post.domain.Post;
import lombok.Builder;

@Builder
public record PostInfoResDto(
        Long id,
        String title,
        String image,
        String content
) {
    // Post 엔티티를 PostInfoResponse DTO로 변환하는 메서드
    public static PostInfoResDto from(Post post) {
        return PostInfoResDto.builder()
                .id(post.getId())           // 게시글 ID 설정
                .title(post.getTitle())     // 게시글 제목 설정
                .image(post.getImage())     // 이미지 URL 설정
                .content(post.getContent()) // 게시글 내용 설정
                .build();
    }

}
