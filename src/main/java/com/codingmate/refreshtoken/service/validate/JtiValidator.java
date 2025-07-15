package com.codingmate.refreshtoken.service.validate;

import com.codingmate.exception.dto.ErrorMessage;
import com.codingmate.exception.exception.jwt.JitNotMatch;
import com.codingmate.exception.exception.jwt.RefreshTokenIsRevoked;
import com.codingmate.refreshtoken.service.RefreshTokenService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 리프레쉬 토큰의 jti를 검증하기 위한 클래스
 *
 * <li>검증을 통과하지 못한다면 예외 발생</li>
 *
 * @author duskafka
 * */
@Slf4j
@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class JtiValidator {
    private final RefreshTokenService refreshTokenService;

    /**
     * jti가 유효한 값인지 검사합니다.
     * 1. 토큰의 jti 와 redis의 jti가 동일한지 검사합니다.
     * 2. jti가 재사용되는지 검사합니다.
     *
     * @param tokenJti 사용자의 쿠키에서 가져온 토큰의 jti
     * @param redisJti redis에 저장되어있는 jti
     * @param programmerId 사용자의 쿠키에서 가져온 토큰의 programmerId
     * */
    public void validateJti(
            String tokenJti,
            String redisJti,
            Long programmerId
    ) {
        log.debug("[RefreshService] validateJti({}, {}, {})", tokenJti, redisJti, programmerId);

        validateJtiEquality(tokenJti, redisJti);
        validateJtiReusability(tokenJti, programmerId);
        log.info("[RefreshService] jti not revoked");
    }

    /**
     * jti가 동일한지 검사하고, 만약 동일하지 않다면 예외를 발생시킵니다.
     *
     * @exception JitNotMatch jti가 일치하지 않을 때 발생시키는 예외
     * */
    private void validateJtiEquality(String tokenJti, String redisJti) {
        if (!tokenJti.equals(redisJti)) {
            log.warn("[RefreshService] JTI mismatch: token = {}, redis = {}", tokenJti, redisJti);
            throw new JitNotMatch(ErrorMessage.JTI_NOT_MATCH, "jti가 일치하지 않습니다");
        }
    }

    /**
     * jti가 재사용되는지 검증합니다. 만약 재사용되는 jti라면 유저의 모든 리프레쉬 토큰을 revoke합니다.
     *
     * @exception RefreshTokenIsRevoked 이미 사용된 jti라면 예외를 발생시킨다
     * */
    private void validateJtiReusability(String jti, Long userId) {
        if (refreshTokenService.isUsedJti(jti)) {
            log.warn("[RefreshService] Reused JTI. All tokens revoked for user: {}", userId);
            refreshTokenService.revokeAllToken(userId);
            throw new RefreshTokenIsRevoked(
                    ErrorMessage.REFRESH_TOKEN_REVOKED,
                    "요청한 리프레시 토큰은 이미 사용된 토큰입니다. 보안 상 이유로 모든 토큰을 무효화합니다."
            );
        }
    }
}
