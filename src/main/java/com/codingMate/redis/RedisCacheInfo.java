package com.codingMate.redis;

import com.codingMate.domain.authority.Authority;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;

@Setter
@Getter
public class RedisCacheInfo implements Serializable {
    private String loginId;
    private Collection<? extends GrantedAuthority> authorities;
    private LocalDateTime issuedAt;
    private LocalDateTime expiresAt;

    @Builder
    public RedisCacheInfo(String loginId, Collection<? extends GrantedAuthority> authorities, LocalDateTime issuedAt, LocalDateTime expiresAt) {
        this.loginId = loginId;
        this.authorities = authorities;
        this.issuedAt = issuedAt;
        this.expiresAt = expiresAt;
    }
}