package com.likelion.oauth2test.service;

import com.google.gson.Gson;
import com.likelion.oauth2test.global.dto.Token;
import com.likelion.oauth2test.global.dto.UserInfo;
import com.likelion.oauth2test.global.jwt.TokenProvider;
import com.likelion.oauth2test.user.domain.Role;
import com.likelion.oauth2test.user.domain.User;
import com.likelion.oauth2test.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthLoginService {
    /* 콘솔에 출력(테스트용)
    public void socialLogin(String code, String registrationId) {
        System.out.println("code = " + code);
        System.out.println("registrationId = " + registrationId);
    }*/
    @Value("${client-id}")
    private String GOOGLE_CLIENT_ID;

    @Value("${client-secret}")
    private String GOOGLE_CLIENT_SECRET;

    private final String GOOGLE_TOKEN_URL = "https://oauth2.googleapis.com/token";
    private final String GOOGLE_REDIRECT_URI = "http://localhost:8080/login/oauth2/code/google";

    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;

    // 구글 액세스 토큰 가져오기
    public String getGoogleAccessToken(String code) {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> params = Map.of(
                "code", code,
                "scope", "https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/userinfo.email",
                "client_id", GOOGLE_CLIENT_ID,
                "client_secret", GOOGLE_CLIENT_SECRET,
                "redirect_uri", GOOGLE_REDIRECT_URI,
                "grant_type", "authorization_code"
        );

        // 구글 토큰 URL로 post 요청 보내서 액세스 토큰 가져오는 로직
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(GOOGLE_TOKEN_URL, params, String.class);
        if(responseEntity.getStatusCode().is2xxSuccessful()) {  // 요청 성공
            String json = responseEntity.getBody();
            Gson gson = new Gson();

            // json 응답을 token 객체로 변환해서 액세스 토큰 반환
            return gson.fromJson(json, Token.class)
                    .getAccessToken();
        }
        // 요청 실패 예외
        throw new RuntimeException("구글 액세스 토큰을 가져오는데 실패하였습니다.");
    }

    // 로그인, 회원가입
    public Token loginOrSignUp(String googleAccessToken) {
        UserInfo userInfo = getUserInfo(googleAccessToken); // 사용자 정보 가져오기

        if (!userInfo.getVerifiedEmail()) { // 이메일 인증이 안 된 경우
            throw new RuntimeException("이메일 인증이 되지 않은 유저입니다.");
        }
        // 유저가 존재 x, 새로 생성해서 저장
        User user =userRepository.findByEmail(userInfo.getEmail()).orElseGet(() ->
                userRepository.save(User.builder()
                        .email(userInfo.getEmail())
                        .name(userInfo.getName())
                        .pictureUrl(userInfo.getPictureUrl())
                        .role(Role.ROLE_USER)
                        .build())
        );
        return tokenProvider.createToken(user);
    }

    // 구글 액세스 토큰으로 사용자 정보 갖고오기
    public UserInfo getUserInfo(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://www.googleapis.com/oauth2/v2/userinfo?access_token=" + accessToken;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);  // Bearer 뒤에 공백 필요함 -> 아니면 로그인 안 됨
        headers.setContentType(MediaType.APPLICATION_JSON);

        RequestEntity<Void> requestEntity = new RequestEntity<>(headers, HttpMethod.GET, URI.create(url));
        ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            String json = responseEntity.getBody();
            Gson gson = new Gson();
            // json 응답 userinfo 객체로 변환해서 반환
            return gson.fromJson(json, UserInfo.class);
        }

        // 요청 실패 예외
        throw new RuntimeException("유저 정보를 가져오는데 실패했습니다.");
    }
}