package com.sparta.newspeed.controller;

import com.sparta.newspeed.dto.ApiResponseDto;
import com.sparta.newspeed.dto.UserResponseDto;
import com.sparta.newspeed.dto.UserUpdateRequestDto;
import com.sparta.newspeed.jwt.JwtUtil;
import com.sparta.newspeed.security.UserDetailsImpl;
import com.sparta.newspeed.service.UserService;
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

    @GetMapping("/users/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id){
        return ResponseEntity.ok().body(userService.getUserById(id));
    }

    @PutMapping("/users")
    public ResponseEntity<UserResponseDto> updateUser(@RequestBody UserUpdateRequestDto updateRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.ok().body(userService.updateUser(updateRequestDto,userDetails.getUser()));
    }

    @DeleteMapping("users/{id}")
    public ResponseEntity<ApiResponseDto> deleteUser(@PathVariable Long id,@AuthenticationPrincipal UserDetailsImpl userDetails){
        userService.deleteUser(id,userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponseDto("삭제 완료", HttpStatus.OK.value()));
    }
}
