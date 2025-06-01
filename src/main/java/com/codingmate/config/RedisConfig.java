package com.codingmate.config;

import com.codingmate.config.properties.RedisProperties;
import com.codingmate.redis.RedisCacheInfo;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
@EnableCaching
public class RedisConfig {
    private final String host;
    private final int port;

    public RedisConfig(RedisProperties redisProperties) {
        this.host = redisProperties.getHost();
        this.port = redisProperties.getPort();
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(host, port);
    }

    @Bean
    public RedisTemplate<String, RedisCacheInfo> redisTemplate() {
        RedisTemplate<String, RedisCacheInfo> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        return redisTemplate;
    }
}