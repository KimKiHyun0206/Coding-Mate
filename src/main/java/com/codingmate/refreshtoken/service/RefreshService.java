package com.codingmate.refreshtoken.service;

import com.codingmate.auth.dto.response.TokenResponse;
import com.codingmate.auth.service.UserDetailLoginService;
import com.codingmate.jwt.TokenProvider;
import com.codingmate.refreshtoken.dto.request.RefreshTokenCreateRequest;
import com.codingmate.refreshtoken.dto.response.RefreshTokenIssueResponse;
import com.codingmate.refreshtoken.service.validate.JtiValidator;
import com.codingmate.util.JwtUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


/**
 * {@code RefreshService}클래스는 리프레쉬 토큰 재발급을 담당하는 클래스입니다.
 * 핵심 비즈니스 로직으로 토큰 재발급 메소드인 refreshToken이 있으며 주요 메소드인 refreshToken을 보조하기 위한 private 메소드가 있습니다.
 *
 * 주요 기능은 {@code RefreshTokenService}에게 사용해서 토큰 생성, 삭제를 요청하고 jti가 일치하는지 확인하고, 토큰이 유요한지 검사합니다.
 * 그리고 {@code ProgrammerService}에서는 요청한 사용자의 role을 조회하기 위해서 사용하며, {@code TokenProvider}에서 토큰이 유효한지 검증합니다.
 *
 * @author 김기현
 * @see RefreshTokenService
 * @see TokenProvider
 * */
@Slf4j
@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshService {
    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final JtiValidator jtiValidator;
    private final UserDetailLoginService userDetailLoginService;

    /**
     * 주어진 리프레시 토큰을 사용하여 액세스 토큰과 리프레시 토큰을 갱신합니다.
     *
     * @param oldRefreshToken 갱신할 기존 리프레시 토큰
     * @return 새로 발급된 액세스 토큰과 리프레시 토큰을 담은 TokenResponse
     */
    public TokenResponse renewTokens(String oldRefreshToken) {
        log.debug("[RefreshService] refreshTokens({})", oldRefreshToken);

        // 1. 토큰 유효성 검사 및 사용자 정보 추출
        tokenProvider.validateToken(oldRefreshToken);

        String tokenJti = JwtUtil.getJti(oldRefreshToken);
        String username = JwtUtil.getUsername(oldRefreshToken);
        String redisJti = refreshTokenService.getJtiFromRedis(username);

        // 2. jti 비교로 토큰 일치 여부 검증
        jtiValidator.validateJti(
                tokenJti,
                redisJti,
                username
        );

        // 3. 인증 정보 구성
        Authentication authentication = createAuthentication(username);

        // 4. 새로운 토큰 생성
        String newAccessToken = tokenProvider.createAccessToken(authentication);
        RefreshTokenIssueResponse newRefreshToken = tokenProvider.createRefreshToken(username);

        // 5. 기존 리프레쉬 토큰 제거 및 새로운 토큰 저장
        updateRefreshTokenInRedis(
                username,
                oldRefreshToken,
                RefreshTokenCreateRequest.of(newRefreshToken, username)
        );

        log.info("[RefreshService] 리프레쉬 토큰을 사용하여 액세스 토큰 재발급에 성공했습니다.");
        return TokenResponse.of(
                newAccessToken,
                newRefreshToken.refreshToken(),
                newRefreshToken.jti(),
                newRefreshToken.issuedAt()
        );
    }

    private Authentication createAuthentication(String username) {
        UserDetails userDetails = userDetailLoginService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
    }


    /**
     * 기존의 리프레시 토큰에 대한 정보를 지우고 새로운 리프레시 토큰 정보를 저장하기 위한 메소드.
     *
     * @param oldKey 이전에 발급된 리프레쉬 토큰의 사용자 id
     * @param oldRefreshToken 이전의 리프레쉬 토큰
     * @param request 데이터베이스에 리프레시 토큰을 저장하기 위해 필요한 객체
     * */
    private void updateRefreshTokenInRedis(
            String oldKey,
            String oldRefreshToken,
            RefreshTokenCreateRequest request
    ) {
        log.debug("[RefreshService] updateRefreshTokenInRedis({}, {})", oldKey, request.token().substring(0, 20));    //20자만 로깅함
        //1. 기존 토큰 제거
        refreshTokenService.revokeToken(oldRefreshToken);

        //2. 새로운 토큰 저장
        refreshTokenService.create(request);    //생성하며 레디스에도 저장함

        log.info("[RefreshService] Redis에 새로운 리프레쉬 토큰 저장에 성공했습니다.");
    }
}