package com.sparta.newspeed.controller;


import com.sparta.newspeed.dto.ApiResponseDto;
import com.sparta.newspeed.dto.AuthRequestDto;
import com.sparta.newspeed.jwt.JwtUtil;
import com.sparta.newspeed.service.UserService;
import jakarta.servlet.http.HttpServletResponse;

import com.sparta.newspeed.dto.ApiResponseDto;
import com.sparta.newspeed.dto.SignupRequestDto;
import com.sparta.newspeed.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/auth")
@RequiredArgsConstructor

public class UserController {

    private final JwtUtil jwtUtil;
    private final UserService userService;

    //로그인
    @PostMapping("/login")
    public ResponseEntity<ApiResponseDto> login(@RequestBody AuthRequestDto loginRequestDto, HttpServletResponse response) {
        try {
            userService.login(loginRequestDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponseDto("회원을 찾을 수 없습니다.", HttpStatus.BAD_REQUEST.value()));
        }

        // JWT 생성 및 쿠키에 저장 후 Response 객체에 추가
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(loginRequestDto.getUsername(), loginRequestDto.getRole()));

        return ResponseEntity.ok().body(new ApiResponseDto("로그인 성공", HttpStatus.CREATED.value()));}

    @PostMapping("/signup")
    public ResponseEntity<ApiResponseDto> signup(@Valid @RequestBody SignupRequestDto requestDto) {

        try {
            userService.signup(requestDto);
        } catch (IllegalArgumentException e) { // 중복된 username이 있는 경우
            ResponseEntity.badRequest().body(new ApiResponseDto("이미 존재하는 id 입니다. 다른 id를 입력해 주세요", HttpStatus.BAD_REQUEST.value()));
        }

        return ResponseEntity.status(201).body(new ApiResponseDto("회원가입 완료 되었습니다.", HttpStatus.CREATED.value()));

    }
}
