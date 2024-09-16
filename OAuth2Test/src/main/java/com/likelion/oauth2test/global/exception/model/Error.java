package com.likelion.oauth2test.global.exception.model;

import org.springframework.http.HttpStatus;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum Error {



	/**
	 * 404 NOT FOUND
	 */
	CATEGORIES_NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND, "해당 카테고리가 존재하지 않습니다."),
	CATEGORIES_PARENT_NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND, "부모 카테고리가 존재하지 않습니다."),

	MEMBERS_NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND, "해당 유저가 없습니다."),

	ITEMS_NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND, "해당 아이템이 존재하지 않습니다."),
	IMAGE_NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND, "해당 이미지를 찾을 수 없습니다."),

	/**
	 * 400 BAD REQUEST
	 */
	VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "유효성 검사에 맞지 않습니다."),
	PARAMETER_NOT_VALID_ERROR(HttpStatus.BAD_REQUEST, "파라미터가 적절치 않습니다."),
	EXTENSION_NOT_VALID_ERROR(HttpStatus.BAD_REQUEST, "지원하지 않는 확장자입니다."),

	/**
	 * 500 INTERNAL SERVER ERROR
	 */
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "알 수 없는 서버 에러.");

	private final HttpStatus httpStatus;
	private final String message;

	public int getErrorCode() {
		return httpStatus.value();
	}
}
