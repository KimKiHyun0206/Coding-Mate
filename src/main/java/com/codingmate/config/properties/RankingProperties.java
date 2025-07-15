package com.codingmate.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;


/**
 * Redis 키 생성기
 * 생성되는 키 형식: {baseKey}:{dailySuffix}:{YYYY-MM-DD}
 * 예시: ranking:daily:2025-07-14
 */
@ConfigurationProperties(prefix = "ranking")
public record RankingProperties(String key, String daily) {
}