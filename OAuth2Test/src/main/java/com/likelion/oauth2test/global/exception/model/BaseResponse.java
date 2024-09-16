package com.likelion.oauth2test.global.exception.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(force = true)
public class BaseResponse<T> {
	private final int code;
	private final String message;
	private T data;

	public static BaseResponse<?> success(Success success){
		return new BaseResponse<>(success.getHttpStatusCode(), success.getMessage());
	}
	public static <T> BaseResponse<T> success(Success success, T data){
		return new BaseResponse<T>(success.getHttpStatusCode(), success.getMessage(),data);
	}
	public static BaseResponse<?> error(Error error){
		return new BaseResponse<>(error.getErrorCode(), error.getMessage());
	}
	public static BaseResponse<?> error(Error error, String message){
		return new BaseResponse<>(error.getErrorCode(), message);
	}
}
