package com.codingmate.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;



@ConfigurationProperties(prefix = "ranking")
public record RankingProperties(String key, String daily) {
}