package com.sparta.newspeed.jwt;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.newspeed.dto.ApiResponseDto;
import com.sparta.newspeed.security.UserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j(topic = "JWT 검증 및 인가")
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final ObjectMapper objectMapper;

    public JwtAuthorizationFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService, ObjectMapper objectMapper) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.objectMapper = objectMapper;
    }

    // 로그인 토큰없이 시도 시 로그인 안내 메서드
    // 요청을 필터링하고 JWT 의 유효성을 검사하는 메서드 (토큰의 존재 여부, 유효성 검사, 블랙리스트 확인을 수행)
    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {
        String tokenValue = jwtUtil.getJwtFromHeader(req);

        if (StringUtils.hasText(tokenValue)) {
            if (!jwtUtil.validateToken(tokenValue)) { // false 일 때
                ApiResponseDto responseDto = new ApiResponseDto("토큰이 유효하지 않습니다.", HttpStatus.BAD_REQUEST.value());
                res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                res.setContentType("application/json; charset=UTF-8");
                res.getWriter().write(objectMapper.writeValueAsString(responseDto));

                return;
            }
            // 블랙리스트에 존재하는 토큰일 경우 조건문에 true 입력, 로그아웃된 토큰 메세지와  인증불가 코드 반환
            if (jwtUtil.isTokenBlacklisted(tokenValue)) {
                ApiResponseDto responseDto = new ApiResponseDto("로그아웃된 토큰입니다.", HttpStatus.UNAUTHORIZED.value());
                res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                res.setContentType("application/json; charset=UTF-8");
                res.getWriter().write(objectMapper.writeValueAsString(responseDto));
                return;
            }

            Claims info = jwtUtil.getUserInfoFromToken(tokenValue);

            try {
                setAuthentication(info.getSubject()); // JWT 토큰의 서브젝트 값을 추출 : 토큰이 어떤 사용자를 나타내는지 확인
                log.info("JWT 검증필터 실행");
            } catch (Exception e) {
                log.error(e.getMessage());
                return;
            }
        }
        filterChain.doFilter(req, res);
    }

    // 인증 처리 : 인증 정보를 SecurityContextHolder 에 설정하는 메서드
    // Spring Security 는 인증(Authentication)과 인가(Authorization)를 처리하기 위해 보안 컨텍스트를 사용함
    public void setAuthentication(String username) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(username);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);

    }

    // 인증 객체 생성 : UserDetailsServiceImpl 을 사용하여 사용자 정보를 조회하고, UsernamePasswordAuthenticationToken을 생성하여 인증에 사용
    private Authentication createAuthentication(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }


}