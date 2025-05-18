package com.codingMate.redis;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Setter
@Getter
public class RedisCacheInfo implements Serializable {
    private Long id;
    private String authority;
    private LocalDateTime issuedAt;
    private LocalDateTime expiresAt;

    @Builder
    public RedisCacheInfo(Long id, String authority, LocalDateTime issuedAt, LocalDateTime expiresAt) {
        this.id = id;
        this.authority = authority;
        this.issuedAt = issuedAt;
        this.expiresAt = expiresAt;
    }
}