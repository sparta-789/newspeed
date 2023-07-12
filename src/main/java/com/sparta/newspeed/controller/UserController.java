package com.sparta.newspeed.controller;


import com.sparta.newspeed.dto.*;
import com.sparta.newspeed.jwt.JwtUtil;
import com.sparta.newspeed.security.UserDetailsImpl;
import com.sparta.newspeed.service.MailSenderService;
import com.sparta.newspeed.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

//@Controller
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final MailSenderService mailSenderService;

    //회원가입
    @PostMapping("/auth/signup")
    public ResponseEntity<ApiResponseDto> signup(@RequestBody SignupRequestDto requestDto) { //클라이언트로부터 SignupRequestDto 를 요청 RequestBody 로 받아와서 처리

        try {
            String authKey= mailSenderService.sendSimpleMessage(requestDto.getEmail());
            requestDto.setAuthKey(authKey);
            userService.signup(requestDto); //회원 가입을 처리하기 위해 userService.signup(requestDto)를 호출
        } catch (IllegalArgumentException e) { // 중복된 username 이 있는 경우
            return ResponseEntity.badRequest().body(new ApiResponseDto("이미 존재하는 id 입니다. 다른 id를 입력해 주세요", HttpStatus.BAD_REQUEST.value()));
        }
        return ResponseEntity.status(201).body(new ApiResponseDto("회원가입 완료 되었습니다.", HttpStatus.CREATED.value()));
    }

    //메일 확인
    @GetMapping("/auth/confirmSignup")
    public ResponseEntity<ApiResponseDto> viewConfirmEmail(@RequestParam String email,@RequestParam String authKey) { //email 과 authKey 를 쿼리 파라미터(RequestParam)로 받아와서 처리
        userService.confirmEmail(email,authKey); // 이메일 주소와 인증 키를 사용하여 사용자의 이메일 인증

        //return "redirect:/login";
        return ResponseEntity.ok().body(new ApiResponseDto("이메일 인증 성공", HttpStatus.OK.value()));
    }

    //로그인
    @PostMapping("/auth/login")
    public ResponseEntity<ApiResponseDto> login(@RequestBody AuthRequestDto loginRequestDto, HttpServletResponse response) {
        try {
            userService.login(loginRequestDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponseDto(e.getLocalizedMessage(), HttpStatus.BAD_REQUEST.value()));
        }

        // JWT 생성 및 쿠키에 저장 후 Response 객체에 추가
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(loginRequestDto.getUsername(), loginRequestDto.getRole()));

        return ResponseEntity.ok().body(new ApiResponseDto("로그인 성공", HttpStatus.OK.value()));
    }


    // 로그아웃
    @PostMapping("/logout")
    // 헤더에서 Authorization 받아오기
    public ResponseEntity<ApiResponseDto> logout(@RequestHeader("Authorization") String authorizationHeader) {
        String token = extractTokenFromHeader(authorizationHeader);
        // 로그아웃 처리 수행
        userService.logout(token);
        return ResponseEntity.ok().body(new ApiResponseDto("로그아웃 성공", HttpStatus.OK.value()));
    }

    // 헤더 값에서 토큰 반환
    private String extractTokenFromHeader(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }

    //유저 조회
    @GetMapping("/users/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok().body(userService.getUserById(id));
    }

    //유저 수정
    @PutMapping("/users")
    public ResponseEntity<UserResponseDto> updateUser(@RequestBody UserUpdateRequestDto updateRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok().body(userService.updateUser(updateRequestDto, userDetails.getUser()));
    }

    //유저 삭제
    @DeleteMapping("/users/{id}")
    public ResponseEntity<ApiResponseDto> deleteUser(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.deleteUser(id, userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponseDto("삭제 완료", HttpStatus.OK.value()));
    }
}
