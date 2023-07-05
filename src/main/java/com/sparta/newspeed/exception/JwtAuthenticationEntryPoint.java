package com.sparta.newspeed.exception;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.newspeed.dto.ApiResponseDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import java.io.IOException;
@Component
//JwtAuthenticationEntryPoint 객체가 생성되어 관리
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint { // JWT 인증에 실패한 경우 처리하기 위한 커스텀 핸들러
    private final ObjectMapper objectMapper;

    // 인증 예외가 발생했을 때 호출되며, 클라이언트에게 인증에 실패했음을 알리기 위한 응답을 처리
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        sendErrorResponse(response,"인증에 실패하였습니다.");
    }
    private void sendErrorResponse(HttpServletResponse response, String message) throws IOException {
        response.setCharacterEncoding("utf-8");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(objectMapper.writeValueAsString(new ApiResponseDto(message,response.getStatus())));
    }
}
