package com.likelion.oauth2test.user.domain.service;

import java.net.URI;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.likelion.oauth2test.global.dto.Token;
import com.likelion.oauth2test.global.dto.UserInfo;
import com.likelion.oauth2test.global.jwt.TokenProvider;
import com.likelion.oauth2test.user.domain.Role;
import com.likelion.oauth2test.user.domain.User;
import com.likelion.oauth2test.user.domain.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthLoginService {

	@Value("${client-id}")
	private String GOOGLE_CLIENT_ID;

	@Value("${client-secret}")
	private String GOOGLE_CLIENT_SECRET;

	private final String GOOGLE_TOKEN_URL = "https://oauth2.googleapis.com/token";

	private final String GOOGLE_REDIRECT_URL = "http://localhost:8080/login/oauth2/code/google";

	private final UserRepository userRepository;
	private final TokenProvider tokenProvider;

	public void socialLogin(String code, String registrationId){
		System.out.println("code = " + code);
		System.out.println("registrationId = "+ registrationId);
	}

	public String getGoogleAccessToken(String code){
		RestTemplate restTemplate = new RestTemplate();
		Map<String, String> params = Map.of(
			"code", code,
			"scope", "https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/userinfo.email",
			"client_id", GOOGLE_CLIENT_ID,
			"client_secret", GOOGLE_CLIENT_SECRET,
			"redirect_uri", GOOGLE_REDIRECT_URL,
			"grant_type", "authorization_code"
		);

		ResponseEntity<String> responseEntity =  restTemplate.postForEntity(GOOGLE_TOKEN_URL, params, String.class);
		if (responseEntity.getStatusCode().is2xxSuccessful()){
			String json = responseEntity.getBody();
			Gson gson = new Gson();

			// json 응답을 token 객체로 변환하여 엑세스토큰 변환
			return gson.fromJson(json, Token.class)
				.getAccessToken();
		}
		throw new RuntimeException("구글 엑세스 토큰을 가져오는데 실패.");
	}
	public Token loginOrSignup(String googleAccessToken){
		UserInfo userInfo = getUserInfo(googleAccessToken);

		if(!userInfo.getVerifiedEmail()){
			throw new RuntimeException("이메일 인증이 되지않은 유저.");
		}
		// 유저가 존재 x 새로 생성해서 저장.

		User user = userRepository.findByEmail(userInfo.getEmail()).orElseGet(
			() -> userRepository.save(User.builder()
				.email(userInfo.getEmail())
				.name(userInfo.getName())
				.pictureUrl(userInfo.getPictureUrl())
				.role(Role.ROLE_USER)
				.build()));
		System.out.println(user.getId());

		return tokenProvider.createToken(user);

	}
	public UserInfo getUserInfo(String googleAccessToken){
		RestTemplate restTemplate = new RestTemplate();
		String url = "https://www.googleapis.com/oauth2/v2/userinfo?access_token=" + googleAccessToken;

		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + googleAccessToken);
		headers.setContentType(MediaType.APPLICATION_JSON);

		RequestEntity<Void> requestEntity = new RequestEntity<>(headers, HttpMethod.GET, URI.create(url));
		ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);

		if (responseEntity.getStatusCode().is2xxSuccessful()){
			String json = responseEntity.getBody();
			Gson gson = new Gson();
			return gson.fromJson(json, UserInfo.class);
		}

		throw new RuntimeException("유저 정보 가져오는데 실패.");

	}

}