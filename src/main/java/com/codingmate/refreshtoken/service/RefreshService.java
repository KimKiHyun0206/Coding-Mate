package com.codingmate.refreshtoken.service;

import com.codingmate.auth.dto.response.TokenResponse;
import com.codingmate.common.annotation.Explanation;
import com.codingmate.jwt.TokenProvider;
import com.codingmate.programmer.service.ProgrammerService;
import com.codingmate.refreshtoken.dto.request.RefreshTokenCreateRequest;
import com.codingmate.refreshtoken.dto.response.RefreshTokenIssueResponse;
import com.codingmate.util.JwtUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


/**
 * {@code RefreshService}클래스는 리프레쉬 토큰 재발급을 담당하는 클래스입니다.
 * 핵심 비즈니스 로직으로 토큰 재발급 메소드인 refreshToken이 있으며 주요 메소드인 refreshToken을 보조하기 위한 private 메소드가 있습니다.
 *
 * 주요 기능은 {@code RefreshTokenService}에게 사용해서 토큰 생성, 삭제를 요청하고 jti가 일치하는지 확인하고, 토큰이 유요한지 검사합니다.
 * 그리고 {@code ProgrammerService}에서는 요청한 사용자의 role을 조회하기 위해서 사용하며, {@code TokenProvider}에서 토큰이 유효한지 검증합니다.
 *
 * @author 김기현
 * @version 1.0.0
 * @see RefreshTokenService
 * @see ProgrammerService
 * @see TokenProvider
 * */
@Slf4j
@Service
@Explanation(
        responsibility = "리프레쉬 토큰을 재발급",
        domain = "RefreshToken",
        lastReviewed = "2025.06.05"
)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshService {
    private final TokenProvider tokenProvider;
    private final ProgrammerService programmerService;
    private final RefreshTokenService refreshTokenService;

    /**
     * 주어진 리프레시 토큰을 사용하여 액세스 토큰과 리프레시 토큰을 갱신합니다.
     *
     * @param oldRefreshToken 갱신할 기존 리프레시 토큰
     * @return 새로 발급된 액세스 토큰과 리프레시 토큰을 담은 TokenResponse
     * @throws com.codingmate.exception.exception.redis.InvalidRefreshTokenException 리프레시 토큰이 유효하지 않거나 Redis에 없는 경우 발생
     * @throws com.codingmate.exception.exception.redis.FailedDeleteRefreshToken 기존 리프레시 토큰 삭제에 실패한 경우 발생
     */
    public TokenResponse refreshTokens(String oldRefreshToken) {
        log.debug("[RedisTokenService] refreshTokens({})", oldRefreshToken);
        log.info("[RedisTokenService] Refresh token renewal request for: {}", oldRefreshToken);
        //1. 토큰이 유효한지 검증한다.
        validateToken(oldRefreshToken);

        //2. 토큰에서 기존에 발급했언 jti와 userID를 추출한다.
        String jtiFromToken = JwtUtil.getJti(oldRefreshToken);
        Long programmerId = JwtUtil.getIdFromSubject(oldRefreshToken);

        //3. Redis에서 값을 userId로 jit를 가져온다
        String jtiFromRedis = refreshTokenService.getJtiFromRedis(programmerId);

        //4. 이전의 jti와 동일한지 확인한다.
        isEqualJti(jtiFromToken, jtiFromRedis);
        log.debug("[RedisTokenService] Successfully retrieved refresh token from Redis: {}", jtiFromRedis.substring(20));

        //5. 가져온 정보로 새로운 액세스 토큰 생성
        String newAccessToken = tokenProvider.createAccessToken(
                programmerId,
                programmerService.getProgrammerRole(programmerId)   //토큰 생성에 필요한 ROLE을 데이터베이스에서 조회
        );

        //6. 새로운 리프레쉬 토큰 생성
        var refreshToken = tokenProvider.createRefreshToken(programmerId);
        log.debug("[RedisTokenService] New access token and refresh token generated.");

        //7. 기존의 토큰을 제거하고 새로운 리프레시 토큰 생성
        renewToken(
                programmerId,
                oldRefreshToken,
                createRequest(refreshToken,  programmerId)
        );

        log.info("[RedisTokenService] Tokens successfully refreshed. New Access Token Length: {}, New Refresh Token Length: {}", newAccessToken.length(), refreshToken.refreshToken().length());
        return TokenResponse.of(newAccessToken, refreshToken.refreshToken(), refreshToken.jti());
    }

    /**
     * 받아온 jti와 서버의 jti가 일치하는지 확인하기 위한 메소드.
     *
     * @param fromToken 사용자가 쿠키에 보낸 토큰에서 가져온 jti
     * @param fromRedis Redis에서 조회한 jti
     * @implNote 동일하지 않다면 블랙리스트에 있는 토큰인지 알아봐야한다.
     * */
    private void isEqualJti(String fromToken, String fromRedis) {
        if (!fromToken.equals(fromRedis)) {
            log.warn("[RedisTokenService] Jti Un Matched: from token = {}, from redis = {}", fromToken, fromRedis);
            //refreshTokenService. // 4-1. 동일하지 않다면 이전에 사용했던 것인지 확인한다.
        }
    }

    /**
     * RefreshTokenCreateRequest 객체를 만드는 메소드.
     * 위의 메소드에 있으면 코드가 난잡하여 분리함.
     *
     * @param id 재발급을 요청한 사용자의 id
     * @param refreshToken 재발급된 리프레시 토큰의 정보
     * */
    private RefreshTokenCreateRequest createRequest(RefreshTokenIssueResponse refreshToken, Long id) {
        return RefreshTokenCreateRequest.of(
                refreshToken.refreshToken(),
                refreshToken.jti(),
                refreshToken.issuedAt(),
                id
        );
    }

    /**
     * 주어진 리프레시 토큰의 유효성을 검증합니다.
     * 유효성 검증은 {@code TokenProvider}를 통해 수행됩니다.
     *
     * @param refreshToken 유효성을 검증할 리프레시 토큰
     */
    private void validateToken(String refreshToken) {
        log.debug("[RedisTokenService] validateToken({})", refreshToken);
        tokenProvider.validateToken(refreshToken);
        log.debug("[RedisTokenService] Refresh token valid.");
    }

    /**
     * 기존의 리프레시 토큰에 대한 정보를 지우고 새로운 리프레시 토큰 정보를 저장하기 위한 메소드.
     *
     * @param oldKey 이전에 발급된 리프레쉬 토큰의 사용자 id
     * @param oldRefreshToken 이전의 리프레쉬 토큰
     * @param request 데이터베이스에 리프레시 토큰을 저장하기 위해 필요한 객체
     *
     * */
    private void renewToken(Long oldKey, String oldRefreshToken, RefreshTokenCreateRequest request) {
        log.debug("[RedisTokenService] renewToken({}, {})", oldKey, request.token().substring(0, 20));    //20자만 로깅함
        //1. 기존 토큰 제거
        refreshTokenService.makeIsRevokedTrue(oldRefreshToken);
        log.debug("[RedisTokenService] Old refresh token deleted from Redis.");

        //2. 새로운 토큰 저장
        refreshTokenService.create(request);    //생성하며 레디스에도 저장함
        log.debug("[RedisTokenService] New refresh token saved to Redis.");
    }
}