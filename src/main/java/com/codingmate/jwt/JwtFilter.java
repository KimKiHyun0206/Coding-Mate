package com.codingmate.jwt;

import com.codingmate.config.properties.JWTProperties;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 이 애플리케이션에서 모든 요청에 대해서 기본적으로 수행될 JWT 필터
 * <li>헤더에서 토큰을 가져와 유효한 자격을 가지고 있다면 SecurityContext에 저장한다</li>
 * <li>애플리케이션은 무상태로 구성되었기 때문에 이 저장 정보는 한 스레드에서만 가지고 있는다</li>
 *
 * @author duskafka
 * */
@Slf4j
public class JwtFilter extends OncePerRequestFilter { // 모든 요청에 대해 동일한 로직을 수행하는 일반 필터

    private final TokenProvider tokenProvider;
    private final TokenValidator tokenValidator;
    private final String ACCESS_TOKEN_HEADER;
    private final String BEARER_HEADER;

    public JwtFilter(TokenProvider tokenProvider, TokenValidator tokenValidator, JWTProperties jwtProperties) {
        this.tokenProvider = tokenProvider;
        this.tokenValidator = tokenValidator;
        this.ACCESS_TOKEN_HEADER = jwtProperties.accessTokenHeader();
        this.BEARER_HEADER = jwtProperties.bearerHeader();
    }

    /**
     * 요청이 들어오면 헤더의 토큰을 검사하고 검사 후 SecurityContextHolder에 정보를 저장해준다.
     *
     * @param request       토큰을 담고 있는 헤더를 가져오기 위해 사용
     * */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String token = resolveToken(request);
        if (token != null && tokenValidator.validateToken(token)) {
            Authentication authentication = tokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearer = request.getHeader(ACCESS_TOKEN_HEADER);
        if (bearer != null && bearer.startsWith(BEARER_HEADER)) {
            return bearer.substring(7);
        }
        return null;
    }
}