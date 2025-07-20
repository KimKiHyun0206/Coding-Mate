package com.codingmate.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "regex")
public record RegexProperties(
        String email,
        String password
) {
}