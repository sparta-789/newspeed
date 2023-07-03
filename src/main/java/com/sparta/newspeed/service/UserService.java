package com.sparta.newspeed.service;

import com.sparta.newspeed.dto.LoginRequestDto;
import com.sparta.newspeed.entity.User;
import com.sparta.newspeed.jwt.JwtUtil;
import com.sparta.newspeed.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserService {
    private  final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserService(User Repository, PasswordEncoder passwordEncoder, JwtUtil jwtutil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtutil;
    }

    public void login(LoginRequestDto requestDto, HttpServletResponse res) {
        String username = requestDto.getUsername();
        String password = requestDto.getPassword();

        // 사용자 확인
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException(" 등록된 사용자가 없습니다.")
        );

        // 비밀번호 확인
        if(!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // JWT 생성 및 쿠키에 저자 후 Response 객체에 추가
        String token = jwtUtil.createToken(user.getUsername(), user.getRole()); // jwt 생성
        jwtUtil.addJwtToCookie(token, res); // 쿠키에 저장 후 Response 객체에 추가
    }
}
