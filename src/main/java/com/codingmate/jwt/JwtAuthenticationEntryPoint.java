package com.codingmate.jwt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * JWT 토큰을 가지고 요청을 했지만 유효한 권한 증명이 없어 요청이 거부되었을 때 호출되는 클래스
 *
 * @author duskafka
 * */
@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    /**
     * 클라이언트가 유효한 자격 증명 없이 보호된 리소스에 접근하려 할 때 호출됩니다.
     * 이 메서드는 HTTP 응답을 '401 Unauthorized' 상태로 설정하고, 인증 실패를 로깅합니다.
     *
     * @param request       인증이 실패한 요청을 나타내는 {@link HttpServletRequest} 객체.
     * @param response      클라이언트에게 응답을 보낼 {@link HttpServletResponse} 객체.
     * @param authException 인증 실패를 초래한 {@link AuthenticationException} 객체.
     * @throws IOException 응답을 보내는 도중 I/O 오류가 발생할 경우.
     */
    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException {
        // 인증 실패 발생 시, 요청 URI와 함께 경고 로그를 남깁니다.
        log.warn("[JwtAuthenticationEntryPoint] Authentication failed for request URI: {}. Error message: {}", request.getRequestURI(), authException.getMessage());
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized: Authentication required.");
    }
}