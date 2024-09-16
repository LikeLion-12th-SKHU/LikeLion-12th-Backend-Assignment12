package com.likelion.oauth2test.feed.controller.dto.request;

import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Parameter;

public record UploadImageRequestDto(
	MultipartFile feedImage,
	String feedTitle,
	String content



) {
}
