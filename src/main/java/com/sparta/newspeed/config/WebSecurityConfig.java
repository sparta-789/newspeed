package com.sparta.newspeed.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.newspeed.jwt.JwtAuthorizationFilter;
import com.sparta.newspeed.jwt.JwtUtil;
import com.sparta.newspeed.security.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration //설정 파일이라는 의미!
@RequiredArgsConstructor
@EnableWebSecurity //Spring Security를 활성화하는 어노테이션
public class WebSecurityConfig {
    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final ObjectMapper objectMapper;
    private final AuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final AccessDeniedHandler customAccessDeniedHandler;

    @Bean //BCryptPasswordEncoder 를 Bean 으로 등록하여 비밀번호를 암호화하는 데 사용
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean //인증 매니저를 Bean 으로 등록하여 인증에 사용
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean // JwtAuthorizationFilter 를 Bean 으로 등록하여 JWT 인증 필터를 사용
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        return new JwtAuthorizationFilter(jwtUtil, userDetailsService,objectMapper);
    }

    @Bean // SecurityFilterChain 을 Bean 으로 등록하여 HTTP 보안 필터 체인을 구성 (핵심기능)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // CSRF 설정
        // csrf : CSRF(Cross-Site Request Forgery) 방어 기능을 비활성화
        // httpBasic : 기본 HTTP 기본 인증을 비활성화
        http.csrf(AbstractHttpConfigurer::disable);
        http.httpBasic(AbstractHttpConfigurer::disable);

        // 기본 설정인 Session 방식은 사용하지 않고 JWT 방식을 사용하기 위한 설정 (세션 관리 방식 설정)
        http.sessionManagement((sessionManagement) ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );
        // authorizeHttpRequests : HTTP 요청에 대한 접근 권한 설정
        // requestMatchers : 특정 요청 매처에 대한 접근 권한 설정을 지정
        http.authorizeHttpRequests((authorizeHttpRequests) ->
                authorizeHttpRequests
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll() // resources 접근 허용 설정
                        .requestMatchers(HttpMethod.GET,"/api/posts/**").permitAll() // 게시글 조회는 인증없이도 가능하기 때문에 허가해준다. -> posts 의 get 요청들 2개 빼고 인가받게 설정
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/users/**").permitAll() // '/api/users/'로 시작하는 요청 모두 접근 허가
                        .anyRequest().authenticated() // 그 외 모든 요청 인증처리

        );
        // 폼로그인 활성화시 코드
        //http.formLogin((formLogin) ->formLogin.loginPage("/api/user/login-page").permitAll());

        // formLogin(): 기존의 폼 로그인을 비활성화
        http.formLogin(AbstractHttpConfigurer::disable);

        // 필터 관리
        // JwtAuthorizationFilter 를 UsernamePasswordAuthenticationFilter 앞에 추가하여 JWT 인증 필터를 적용
        http.addFilterBefore(jwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);

        // 접근 거부 및 인증 예외 처리를 위한 핸들러를 설정
        http.exceptionHandling((exceptionHandling)->exceptionHandling.accessDeniedHandler(customAccessDeniedHandler));
        http.exceptionHandling((exceptionHandling)->exceptionHandling.authenticationEntryPoint(jwtAuthenticationEntryPoint));
        return http.build();        // SecurityFilterChain을 생성
    }
}
