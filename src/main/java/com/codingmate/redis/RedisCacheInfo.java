package com.codingmate.redis;

import java.io.Serializable;
import java.time.LocalDateTime;

public record RedisCacheInfo(
        Long id,
        String authority,
        LocalDateTime issuedAt,
        LocalDateTime expiresAt
) implements Serializable {
    public static RedisCacheInfo of(Long id, String authority, LocalDateTime issuedAt, LocalDateTime expiresAt) {
        return new RedisCacheInfo(id, authority, issuedAt, expiresAt);
    }
}