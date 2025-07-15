package com.codingmate.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;


/**
 * Ranking Redis 키를 가지는 프로퍼티
 * 생성되는 키 형식: {baseKey}:{dailySuffix}:{YYYY-MM-DD}
 * 예시: ranking:daily:2025-07-14
 *
 * @author duskafka
 */
@ConfigurationProperties(prefix = "ranking")
public record RankingProperties(String key, String daily) {
}