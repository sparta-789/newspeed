package com.sparta.newspeed.jwt;


import com.sparta.newspeed.entity.TokenBlacklist;
import com.sparta.newspeed.entity.UserRoleEnum;
import com.sparta.newspeed.repository.TokenBlacklistRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j(topic = "JwtUtil") // 로깅을 위한 Lombok 어노테이션
@Component
public class JwtUtil { // JWT (JSON Web Token)을 생성하고 검증하는 클래스
    // JwtUtil : JWT 토큰의 생성, 헤더에서 토큰 추출, 토큰의 유효성 검사, 토큰에서 사용자 정보 추출, 토큰 블랙리스트 확인 등의 기능을 제공
    // Header KEY 값
    public static final String AUTHORIZATION_HEADER = "Authorization";
    // 사용자 권한 값의 KEY
    public static final String AUTHORIZATION_KEY = "auth";
    // Token 식별자
    public static final String BEARER_PREFIX = "Bearer ";
    // 토큰 만료시간
    private final long TOKEN_TIME = 60 * 60 * 1000L; // 60분

    // 로그아웃 처리 시 토큰 블랙리스트에 대한 repository
    // 토큰 블랙리스트 : 사용된 토큰을 사용하지 못하게 저장하여 관리함
    @Autowired
    private TokenBlacklistRepository tokenBlacklistRepository;


    @Value("${jwt.secret.key}") // Base64 Encode 한 SecretKey
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;


    @PostConstruct // 인스턴스 생성 및 의존성 주입이 완료된 후에 실행되어야 함
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    } // JwtUtil 인스턴스 생성 후 secretKey 값을 Base64 디코딩하여 key 를 초기화하는 역할

    // 주어진 사용자 이름과 권한 정보를 기반으로 JWT 토큰을 생성
    public String createToken(String username, UserRoleEnum role) {
        Date date = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(username) // 사용자 식별자값(ID)
                        .claim(AUTHORIZATION_KEY, role) // 사용자 권한
                        .setExpiration(new Date(date.getTime() + TOKEN_TIME)) // 만료 시간
                        .setIssuedAt(date) // 발급일
                        .signWith(key, signatureAlgorithm) // 암호화 알고리즘
                        .compact();
    }

    //  HTTP 요청의 헤더에서 JWT 토큰 가져오기
    public String getJwtFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER); // AUTHORIZATION_HEADER : JWT 토큰을 HTTP 요청의 헤더에 포함시키기 위한 헤더의 이름
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) { // BEARER_PREFIX 상수는 이러한 접두사 문자열을 나타내며, JWT 토큰을 포함시킬 때 접두사와 토큰 값을 조합하여 사용
            return bearerToken.substring(7);
        }
        return null;
    }

    // 주어진 토큰의 유효성을 검사 (토큰 검증)
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token); // key 를 사용하여 토큰의 서명을 확인
            return true; // 유효한 토큰인 경우
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }

    // 토큰에서 사용자 정보 가져오기
    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }
    // 블랙리스트에 토큰이 있는지 확인, 존재하면 != null 즉 true 반환
    public boolean isTokenBlacklisted(String tokenValue) {
        TokenBlacklist tokenBlacklist = tokenBlacklistRepository.findByToken(tokenValue).orElse(null);
        return tokenBlacklist != null;
    }
}