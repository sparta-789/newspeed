package com.sparta.newspeed.exception;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.newspeed.dto.ApiResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;
@Component
//스프링 애플리케이션 컨텍스트에 CustomAccessDeniedHandler 객체가 생성되어 관리
@Slf4j
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    //스프링 프레임워크에서 권한이 없는 사용자 접근 시 발생하는 예외를 처리하는 커스텀 핸들러 클래스
    private final ObjectMapper objectMapper; //JSON 데이터를 Java 객체로 변환하거나 Java 객체를 JSON 데이터로 변환하는 데 사용되는 Jackson 라이브러리의 ObjectMapper 객체
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        sendErrorResponse(response,"권한이 없습니다.");
    }
    private void sendErrorResponse(HttpServletResponse response, String message) throws IOException {
        response.setCharacterEncoding("utf-8");
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(objectMapper.writeValueAsString(new ApiResponseDto(message,response.getStatus())));
    }
}