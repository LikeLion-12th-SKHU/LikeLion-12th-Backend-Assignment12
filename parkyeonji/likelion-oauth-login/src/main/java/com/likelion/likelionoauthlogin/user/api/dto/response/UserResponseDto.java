package com.likelion.likelionoauthlogin.user.api.dto.response;

import com.likelion.likelionoauthlogin.user.domain.User;
import lombok.Builder;

@Builder
public record UserResponseDto(
        Long id,
        String name,
        String email,
        String pictureUrl
) {
    public static UserResponseDto from(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .pictureUrl(user.getPictureUrl())
                .build();
    }
}
