package com.likelion.oauth2test.global.jwt;

import com.likelion.oauth2test.global.dto.Token;
import com.likelion.oauth2test.user.domain.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;


import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TokenProvider {
    private static final String AUTHORITIES_KEY = "auth";   // .claim("auth"~) 로 써도 됨
    private final Key key;
    private final long accessTokenValidityTime; // 토큰 유효 시간

    @Autowired
    public TokenProvider(@Value("${jwt.secret}") String secretKey,
                         @Value("${jwt.access-token-validity-in-milliseconds}") long accessTokenValidityTime) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);   // secretKey를 Base64 디코딩하여 바이트 배열로 변환
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenValidityTime = accessTokenValidityTime;
    }

    // 정보와 시크릿 키, 시간을 넣어 압축해 JWT 토큰 생성
    public Token createToken(User user) {
        long nowTime = (new Date()).getTime();
        // 토큰 만료 시간 설정
        Date tokenExpiredTime = new Date(nowTime + accessTokenValidityTime);
        // 토큰 생성
        String accessToken = Jwts.builder()
                .setSubject(user.getId().toString())    // userId를 주체로 설정
                .claim(AUTHORITIES_KEY, user.getRole().name())  // 사용자 권한(role)을 claim 객체에 추가
                .setExpiration(tokenExpiredTime)    // 만료 시간 설정
                .signWith(key, SignatureAlgorithm.HS256)    // 키, 서명 알고리즘
                .compact(); // 압축
        // 토큰 리턴
        return Token.builder()
                .accessToken(accessToken)
                .build();
    }

    // JWT 토큰에서 인증 정보를 추출하여 Authentication 객체를 반환하는 메서드.
    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken);

        if (claims.get(AUTHORITIES_KEY) == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }
        // 권한 정보 만들기, ","로 구분해서 리스트로
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
        // 유저 이름, 비밀번호 권한 들어있는 객체 생성하고 리턴
        return new UsernamePasswordAuthenticationToken(claims.getSubject(), "", authorities);
    }

    // 토큰 추출
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }

    // 토큰 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);

            return true;
        } catch (UnsupportedJwtException | ExpiredJwtException | IllegalArgumentException e) {
            // likelion-jwt-login 에서는 log도 찍고 exception 메세지도 출력함
            return false;
        }
    }

    // 토큰에서 claim 파싱
    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}