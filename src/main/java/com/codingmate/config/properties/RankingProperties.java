package com.codingmate.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.LocalDate;

@ConfigurationProperties(prefix = "ranking")
public record RankingProperties(String key, String daily) {
    public String getKey() {
        return key + ":" + daily + ":" + LocalDate.now();
    }
}