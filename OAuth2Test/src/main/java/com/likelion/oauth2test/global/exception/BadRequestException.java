package com.likelion.oauth2test.global.exception;

import com.likelion.oauth2test.global.exception.model.CustomException;
import com.likelion.oauth2test.global.exception.model.Error;

public class BadRequestException extends CustomException {

	public BadRequestException(Error error, String message){
		super(error,message);
	}
}
