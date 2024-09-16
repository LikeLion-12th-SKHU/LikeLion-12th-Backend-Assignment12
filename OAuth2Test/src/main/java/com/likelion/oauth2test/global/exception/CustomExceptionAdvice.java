package com.likelion.oauth2test.global.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.likelion.oauth2test.global.exception.model.BaseResponse;
import com.likelion.oauth2test.global.exception.model.CustomException;
import com.likelion.oauth2test.global.exception.model.Error;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class CustomExceptionAdvice {

	/**
	 * 500 Internal Server Error
	 */
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Exception.class)
	public BaseResponse<?> handlerServerException(final Exception e){
		log.error("Internal Server Error: {}", e.getMessage(), e);
		return BaseResponse.error(Error.INTERNAL_SERVER_ERROR);
	}

	/**
	 * Custom Error 400
	 */

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public BaseResponse<?> handleValidationException(MethodArgumentNotValidException e){
		// 에러 메시지 생성
		return BaseResponse.error(Error.VALIDATION_ERROR, convertMapToString(createBindErrorMessage(e)));
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public BaseResponse<?> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e){
		log.error("Custom Exception: {}", e.getMessage(), e);
		return BaseResponse.error(Error.PARAMETER_NOT_VALID_ERROR, Error.PARAMETER_NOT_VALID_ERROR.getMessage());
	}

	@ExceptionHandler(CustomException.class)
	public BaseResponse<?> handlerCustomException(CustomException e){
		log.error("Custom Exception: {}", e.getMessage(), e);
		return BaseResponse.error(e.getError(), e.getMessage());
	}

	private Map<String,String> createBindErrorMessage(BindException e){
		Map<String, String> errorMap = new HashMap<>();
		for (FieldError fieldError: e.getBindingResult().getFieldErrors()){
			errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
		}
		return errorMap;
	}

	private String convertMapToString(Map<String, String> map){
		StringBuilder sb = new StringBuilder();
		for(Map.Entry<String, String> entry : map.entrySet()){
			sb.append(entry.getKey()).append(" : ").append(entry.getValue()).append(", ");
		}
		return sb.substring(0,sb.length()-2);
	}

}
