package com.sparta.newspeed.controller;


import com.sparta.newspeed.dto.*;
import com.sparta.newspeed.jwt.JwtUtil;
import com.sparta.newspeed.security.UserDetailsImpl;
import com.sparta.newspeed.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    //회원가입
    @PostMapping("/auth/signup")
    public ResponseEntity<ApiResponseDto> signup(@Valid @RequestBody SignupRequestDto requestDto) {

        try {
            userService.signup(requestDto);
        } catch (IllegalArgumentException e) { // 중복된 username이 있는 경우
            return ResponseEntity.badRequest().body(new ApiResponseDto("이미 존재하는 id 입니다. 다른 id를 입력해 주세요", HttpStatus.BAD_REQUEST.value()));

        }

        return ResponseEntity.status(201).body(new ApiResponseDto("회원가입 완료 되었습니다.", HttpStatus.CREATED.value()));
    }

    //로그인
    //Todo USerService 에서는 아이디와 비밀번호가 다른 경우 각각 예외메시지 처리를 하고 있으나 Controller 에서는 ID/PW 예외메시지가 묶여서 한번에 처리됨
    @PostMapping("/auth/login")
    public ResponseEntity<ApiResponseDto> login(@RequestBody AuthRequestDto loginRequestDto, HttpServletResponse response) {
        try {
            userService.login(loginRequestDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponseDto(e.getLocalizedMessage(), HttpStatus.BAD_REQUEST.value()));
        }

        // JWT 생성 및 쿠키에 저장 후 Response 객체에 추가
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(loginRequestDto.getUsername(), loginRequestDto.getRole()));

        return ResponseEntity.ok().body(new ApiResponseDto("로그인 성공", HttpStatus.CREATED.value()));}

    //유저 조회
    @GetMapping("/users/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id){
        return ResponseEntity.ok().body(userService.getUserById(id));
    }

    //유저 수정
    @PutMapping("/users")
    public ResponseEntity<UserResponseDto> updateUser(@RequestBody UserUpdateRequestDto updateRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.ok().body(userService.updateUser(updateRequestDto,userDetails.getUser()));
    }

    //유저 삭제
    @DeleteMapping("users/{id}")
    public ResponseEntity<ApiResponseDto> deleteUser(@PathVariable Long id,@AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.deleteUser(id, userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponseDto("삭제 완료", HttpStatus.OK.value()));
    }

}
