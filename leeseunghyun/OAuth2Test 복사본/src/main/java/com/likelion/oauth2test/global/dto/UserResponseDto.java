package com.likelion.oauth2test.global.dto;

public record UserResponseDto(
        Long id,
        String name,
        String imageFile
) {
    public static UserResponseDto of(Long id, String name, String imageFile) {
        return new UserResponseDto(id, name, imageFile);
    }
}
