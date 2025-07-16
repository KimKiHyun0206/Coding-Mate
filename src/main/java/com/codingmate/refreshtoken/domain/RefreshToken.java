package com.codingmate.refreshtoken.domain;

import com.codingmate.refreshtoken.dto.request.RefreshTokenCreateRequest;
import jakarta.persistence.*;
import lombok.*;

import java.time.Duration;
import java.time.Instant;


/**
 * 리프레쉬 토큰 블랙리스트를 구현하기 위한 도메인.
 *
 * <ul>
 *     <li>id: 리프레쉬 토큰의 PK</li>
 *     <li>token: 리프레쉬 토큰 전문</li>
 *     <li>jti: 리프래쉬 토큰을 구분하기 위한 UUID</li>
 *     <li>issuedAt: 발급된 시간</li>
 *     <li>expiredAt: 유효기간</li>
 *     <li>isRevoked: 토큰이 사용되었는지</li>
 *     <li>username: 발급한 사용자의 로그인 아이디</li>
 * </ul>
 * */
@Entity
@Table(name = "refresh_token_info",
        indexes = {
                @Index(name = "ref_jti_idx", columnList = "jti", unique = true),
                @Index(name = "ref_user_revoke_idx", columnList = "username, is_revoked")
        }
)
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 실제 JWT 토큰 문자열 (긴 길이 가능)
    @Column(name = "token", nullable = false, length = 500) // 토큰 문자열의 길이 고려
    private String token;

    // JWT ID - Redis의 jti와 동일한 값
    @Column(name = "jti", nullable = false, unique = true, length = 36) // UUID는 36자리 고정
    private String jti;

    // 토큰 발급 시각 (Epoch 밀리초 또는 Instant)
    @Column(name = "issued_at", nullable = false)
    private Instant issuedAt; // Instant가 더 현대적인 Java Date/Time API

    // 토큰 만료 시각
    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt; // Instant가 더 현대적인 Java Date/Time API

    // 토큰이 폐기되었는지 여부 (블랙리스트와 연동)
    @Column(name = "is_revoked", nullable = false)
    private boolean isRevoked = false; // 기본값은 false (폐기되지 않음)

    // 이 토큰이 속한 사용자 ID (인덱싱을 고려하면 성능에 좋음)
    @Column(name = "username", nullable = false)
    private String username;

    public static RefreshToken toEntity(RefreshTokenCreateRequest request, int expireDay) {
        return RefreshToken.builder()
                .token(request.token())
                .jti(request.jti())
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plus(Duration.ofDays(expireDay)))
                .username(request.username())
                .build();
    }

    public void revoke() {
        this.isRevoked = true;
    }
}
