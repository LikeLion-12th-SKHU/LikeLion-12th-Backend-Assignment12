package com.likelion.oauth2test.global.exception.model;

import org.springframework.http.HttpStatus;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum Success {


	/**
	 * 200 OK
	 */
	GET_SUCCESS(HttpStatus.OK, "게시물 찾기 성공"),
	POST_UPDATE_SUCCESS(HttpStatus.OK, "게시물 수정 성공"),
	MEMBER_UPDATE_SUCCESS(HttpStatus.OK, "유저 수정 성공"),
	POST_DELETE_SUCCESS(HttpStatus.OK, "글 삭제 성공"),
	MEMBER_DELETE_SUCCESS(HttpStatus.OK, "글 삭제 성공"),

	/**
	 * 201 CREATED
	 */
	POST_SAVE_SUCCESS(HttpStatus.CREATED, "게시물 생성 성공"),
	MEMBER_SAVE_SUCCESS(HttpStatus.OK, "멤버 생성 성공");

	private final HttpStatus httpStatus;
	private final String message;
	public int getHttpStatusCode(){
		return httpStatus.value();
	}
}
