package com.codingmate.config;

import com.codingmate.config.properties.RedisProperties;
import com.codingmate.redis.RefreshTokenDetail;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

@Configuration
@EnableCaching
public class RedisConfig {
    private final String host;
    private final int port;

    public RedisConfig(RedisProperties redisProperties) {
        this.host = redisProperties.host();
        this.port = redisProperties.port();
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(host, port);
    }

    @Bean
    public RedisTemplate<String, RefreshTokenDetail> redisTemplate() {
        RedisTemplate<String, RefreshTokenDetail> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        return redisTemplate;
    }

    @Bean
    public ValueOperations<String, RefreshTokenDetail> valueOperations() {
        return redisTemplate().opsForValue();
    }
}