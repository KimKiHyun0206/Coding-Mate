package com.codingmate.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Redis 연결을 위한 프로퍼티를 가지는 클래스
 *
 * @author duskafka
 * */
@ConfigurationProperties(prefix = "spring.data.redis")
public record RedisProperties(String host, int port) {
}