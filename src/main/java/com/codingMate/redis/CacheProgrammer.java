package com.codingMate.redis;

import com.codingMate.domain.authority.Authority;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.Set;

@Getter
@RedisHash(value = "programmer", timeToLive = 10000)
@AllArgsConstructor
public class CacheProgrammer implements Serializable {
    @Id
    private Long id;
    private String name;
    private Set<Authority> authorities;
}