package com.codingmate.redis;

import java.io.Serializable;
import java.time.LocalDateTime;

public record RefreshTokenDetail(
        Long id,
        String authority,
        LocalDateTime issuedAt,
        LocalDateTime expiresAt
) implements Serializable {
    public static RefreshTokenDetail of(Long id, String authority, LocalDateTime issuedAt, LocalDateTime expiresAt) {
        return new RefreshTokenDetail(id, authority, issuedAt, expiresAt);
    }
}