package com.codingmate.jwt;

import com.codingmate.exception.exception.jwt.ExpiredTokenException;
import com.codingmate.util.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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

/**
 * 이 애플리케이션에서 모든 요청에 대해서 기본적으로 수행될 JWT 필터
 * <li>헤더에서 토큰을 가져와 유효한 자격을 가지고 있다면 SecurityContext에 저장한다</li>
 * <li>애플리케이션은 무상태로 구성되었기 때문에 이 저장 정보는 한 스레드에서만 가지고 있는다</li>
 *
 * @author duskafka
 * */
@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
public class JwtFilter extends GenericFilterBean { // 모든 요청에 대해 동일한 로직을 수행하는 일반 필터

    private final TokenProvider tokenProvider;

    /**
     * 모든 HTTP 요청에 대해 필터링 작업을 수행합니다.
     * 요청에서 JWT를 추출하고, 유효성을 검증하며, 유효한 경우 Spring Security 컨텍스트에 인증 정보를 설정합니다.
     * 토큰이 만료되었을 경우에는 401 Unauthorized 응답을 보냅니다.
     *
     * @param servletRequest  HTTP 요청을 나타내는 {@link ServletRequest} 객체
     * @param servletResponse HTTP 응답을 나타내는 {@link ServletResponse} 객체
     * @param filterChain     다음 필터 또는 서블릿으로 요청/응답을 전달하는 {@link FilterChain} 객체
     * @throws IOException      I/O 오류 발생 시
     * @throws ServletException 서블릿 관련 오류 발생 시
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // HTTP 요청 및 응답 객체로 캐스팅
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

        // 1. HTTP 요청에서 JWT(Access Token)를 추출합니다.
        String jwt = JwtUtil.getAccessToken(httpServletRequest);
        String requestURI = httpServletRequest.getRequestURI(); // 현재 요청 URI

        log.debug("[JwtFilter] Processing request for URI: {}", requestURI);

        try {
            // 2. 추출된 JWT가 존재하고 유효한지 검증합니다.
            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                // 3. 토큰이 유효하면, 토큰에서 인증 정보를 가져와 Spring Security 컨텍스트에 저장합니다.
                Authentication authentication = tokenProvider.getAuthentication(jwt);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.debug("[JwtFilter] Authentication successful. Stored '{}' authentication in Security Context for URI: {}", authentication.getName(), requestURI);
            } else {
                // 4. JWT가 없거나 유효하지 않은 경우, Security Context에 인증 정보를 저장하지 않습니다.
                log.debug("[JwtFilter] No valid JWT token found for URI: {}. Proceeding without authentication.", requestURI);
            }
            // 5. 현재 필터의 처리가 완료되었으므로, 다음 필터로 요청/응답을 전달합니다.
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (ExpiredTokenException e) {
            // 6. 만료된 토큰 예외가 발생하면, 클라이언트에게 401 Unauthorized 응답을 보냅니다.
            log.info("[JwtFilter] Expired JWT token for URI: {}. Sending 401 Unauthorized response.", requestURI);
            httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            // 만료된 토큰이므로 더 이상 필터 체인을 진행하지 않습니다.
        } catch (Exception e) {
            // 그 외 JWT 관련 예외 발생 시 (예: 서명 오류, 지원되지 않는 토큰 등)
            // TokenProvider.validateToken()에서 이미 특정 예외를 던지므로, 여기서는 일반적인 오류를 처리하거나 추가 로깅을 할 수 있습니다.
            // 현재는 ExpiredTokenException 외에는 여기서 명시적으로 잡지 않아 Spring Security의 AccessDeniedHandler 또는 ExceptionHandler로 전달됩니다.
            log.error("[JwtFilter] An unexpected error occurred during JWT processing for URI: {}. Error: {}", requestURI, e.getMessage());
            // 필요한 경우, 여기서도 httpServletResponse.setStatus()를 통해 특정 오류 코드를 보낼 수 있습니다.
            throw e; // 예외를 다시 던져서 상위 핸들러에서 처리하도록 합니다.
        }
    }
}