package com.likelion.oauth2test.user.domain.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.likelion.oauth2test.global.dto.Token;
import com.likelion.oauth2test.user.domain.service.AuthLoginService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/login/oauth2")
public class AuthLoginController {
	private final AuthLoginService authLoginService;

	// @GetMapping("/code/{registrationId}")
	// public void googleLogin(@RequestParam String code, @PathVariable String registrationId){
	// 	authLoginService.socialLogin(code, registrationId);
	// }
	@GetMapping("/code/google")
	public Token googleCallback(@RequestParam(name = "code") String code){
		String googleAccessToken = authLoginService.getGoogleAccessToken(code);
		return loginOrSignup(googleAccessToken); // 인가코드를 통해 로그인이나 회원가입.
	}

	public Token loginOrSignup(String googleAccessToken){
		return authLoginService.loginOrSignup(googleAccessToken);
	}

}