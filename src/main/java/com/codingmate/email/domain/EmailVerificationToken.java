package com.codingmate.email.domain;

import com.codingmate.util.SecureRandomUtil;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 이메일 인증 정보를 데이터베이스에 저장하는 도메인.
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailVerificationToken {
    /**
     * PK
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 인증 대상 이메일 (토큰이 어떤 이메일을 위한 것인지 명시)
     */
    private String email;

    /**
     * 이메일 인증에 사용되는 토큰 값
     */
    private String token;

    /**
     * 인증 토큰 만료 시각
     */
    private LocalDateTime expiry;

    /**
     * 인증이 완료되었는지 여부
     */
    private boolean verified;

    /**
     * 이메일을 입력하면 랜덤한 토큰을 유틸 클래스에서 발급 받는다.
     * 그리고 유효 시간을 10분으로 설정하고 인증 여부는 false로 설정한다.
     *
     * @param email 인증이 필요한 이메일
     * */
    public EmailVerificationToken(String email) {
        this.email = email;
        token = SecureRandomUtil.generateToken(16);
        expiry = LocalDateTime.now().plusMinutes(10);
        verified = false;
    }

    /**
     * 토큰이 만료되었는지 확인하는 메소드
     */
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiry);
    }

    /**
     * 인증 완료 처리를 하기 위한 메소드
     */
    public void markAsVerified() {
        this.verified = true;
    }
}