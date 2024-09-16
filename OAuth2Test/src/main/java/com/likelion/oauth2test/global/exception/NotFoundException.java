package com.likelion.oauth2test.global.exception;

import com.likelion.oauth2test.global.exception.model.CustomException;
import com.likelion.oauth2test.global.exception.model.Error;

public class NotFoundException extends CustomException {

	public NotFoundException(Error error, String message){
		super(error,message);
	}
}
