package com.sparta.newspeed.controller;


import com.sparta.newspeed.dto.*;
import com.sparta.newspeed.jwt.JwtUtil;
import com.sparta.newspeed.security.UserDetailsImpl;
import com.sparta.newspeed.service.MailSenderService;
import com.sparta.newspeed.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

//@Controller
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final MailSenderService mailSenderService;

//    @Controller
    //Todo 회원가입이 성공적으로 완료되면  리다이렉션할 URL을 클라이언트에게 전달
//    public class SignupController {
//        @Autowired
//        private RedirectView mainPageRedirect; // 메인 페이지로 리다이렉션하기 위한 객체
//
//        @PostMapping("/signup")
//        @ResponseBody
//        public ResponseEntity<ApiResponseDto> signup(@RequestBody SignupRequestDto request) {
//            // 받아온 회원 정보를 DB에 저장하는 로직을 구현해야 합니다.
//            // 이 예시에서는 단순히 받아온 회원 정보를 출력하는 것으로 대체합니다.
//
//            // 회원가입이 성공적으로 완료되면 메인 페이지로 리다이렉션
//            return ResponseEntity.ok


            //회원가입
    @PostMapping("/auth/signup")
    public ResponseEntity<ApiResponseDto> signup(@RequestBody SignupRequestDto requestDto) {

        try {
            String authKey= mailSenderService.sendSimpleMessage(requestDto.getEmail());
            requestDto.setAuthKey(authKey);
            userService.signup(requestDto);
        } catch (IllegalArgumentException e) { // 중복된 username 이 있는 경우
            return ResponseEntity.badRequest().body(new ApiResponseDto("이미 존재하는 id 입니다. 다른 id를 입력해 주세요", HttpStatus.BAD_REQUEST.value()));
        }
        return ResponseEntity.status(201).body(new ApiResponseDto("회원가입 완료 되었습니다.", HttpStatus.CREATED.value()));
    }

    //메일 확인
    @GetMapping("auth/confirmSignup")
    public ResponseEntity<ApiResponseDto> viewConfirmEmail(@RequestParam String email,@RequestParam String authKey) {
        userService.confirmEmail(email,authKey);

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

        return ResponseEntity.ok().body(new ApiResponseDto("로그인 성공", HttpStatus.CREATED.value()));
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
    @DeleteMapping("users/{id}")
    public ResponseEntity<ApiResponseDto> deleteUser(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.deleteUser(id, userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponseDto("삭제 완료", HttpStatus.OK.value()));
    }
}
