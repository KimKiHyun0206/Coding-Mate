package com.codingmate.jwt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@Slf4j
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    /**
     * 인증된 사용자가 필요한 권한 없이 보호된 리소스에 접근하려 할 때 호출됩니다.
     * 이 메서드는 HTTP 응답을 '403 Forbidden' 상태로 설정하고, 접근 거부 발생을 로깅합니다.
     *
     * @param request           접근이 거부된 요청을 나타내는 {@link HttpServletRequest} 객체.
     * @param response          클라이언트에게 응답을 보낼 {@link HttpServletResponse} 객체.
     * @param accessDeniedException 접근 거부를 초래한 {@link AccessDeniedException} 객체.
     * @throws IOException 응답을 보내는 도중 I/O 오류가 발생할 경우.
     */
    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException
    ) throws IOException {
        log.warn("[JwtAccessDeniedHandler] Access Denied for request URI: {}. Error message: {}", request.getRequestURI(), accessDeniedException.getMessage());
        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden: You do not have sufficient permissions to access this resource.");
    }
}