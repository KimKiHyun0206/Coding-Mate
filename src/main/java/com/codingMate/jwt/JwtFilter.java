package com.codingMate.jwt;

import com.codingMate.exception.exception.jwt.ExpiredTokenException;
import com.codingMate.util.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
public class JwtFilter extends GenericFilterBean {

    @Value("${jwt.header}")
    private String AUTHORIZATION_HEADER;
    private final TokenProvider tokenProvider;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        String jwt = JwtUtil.getAccessToken(httpServletRequest);
        String requestURI = httpServletRequest.getRequestURI();

        try {
            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                Authentication authentication = tokenProvider.getAuthentication(jwt);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.debug("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}", authentication.getName(), requestURI);
            } else {
                log.debug("유효한 JWT 토큰이 없습니다, uri: {}", requestURI);
            }
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (ExpiredTokenException e) { //만료된 토큰일 시
            log.info("만료된 JWT 토큰입니다, uri: {}", requestURI);
            httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}