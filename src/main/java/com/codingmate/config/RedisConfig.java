package com.codingmate.config;

import com.codingmate.config.properties.RedisProperties;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableCaching
public class RedisConfig {
    private final String host;
    private final int port;

    public RedisConfig(
            RedisProperties redisProperties
    ) {
        this.host = redisProperties.host();
        this.port = redisProperties.port();
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(host, port);
    }

    @Bean
    public RedisTemplate<String, String> myStringRedisTemplate() {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());

        //Key와 Value 직렬화 설정
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());
        return redisTemplate;
    }

    // Sorted Set Operations 빈 (활성 토큰 관리에 사용)
    // ZSetOperations는 RedisTemplate<String, String>에서 바로 가져올 수 있습니다.
    @Bean
    public ZSetOperations<String, String> zSetOperations(
            @Qualifier("myStringRedisTemplate") RedisTemplate<String, String> redisTemplate
    ) {
        return redisTemplate.opsForZSet();
    }

    // Value Operations 빈 (블랙리스트 관리에 사용 - String 타입 값)
    // ValueOperations도 RedisTemplate<String, String>에서 바로 가져올 수 있습니다.
    @Bean
    public ValueOperations<String, String> stringValueOperations(
            @Qualifier("myStringRedisTemplate") RedisTemplate<String, String> redisTemplate) {
        return redisTemplate.opsForValue();
    }

    // 랭킹용 Redis 저장소
    @Bean
    public RedisTemplate<String, Object> objectRedisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer()); // JSON 자동 직렬화
        return template;
    }

    @Bean
    public ValueOperations<String, Object> objectValueOperations(
            @Qualifier("objectRedisTemplate") RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForValue();
    }
}