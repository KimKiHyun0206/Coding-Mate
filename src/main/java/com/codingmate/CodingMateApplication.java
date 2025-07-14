package com.codingmate;

import com.codingmate.config.properties.JWTProperties;
import com.codingmate.config.properties.RankingProperties;
import com.codingmate.config.properties.RedisProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;

import java.util.Arrays;
import java.util.TimeZone;

@Slf4j
@SpringBootApplication
@RequiredArgsConstructor
@EnableConfigurationProperties({
        RedisProperties.class,
        JWTProperties.class,
        RankingProperties.class
})
public class CodingMateApplication implements ApplicationListener<ApplicationReadyEvent> {
    private final Environment environment;

    public static void main(String[] args) {
        init();
        SpringApplication.run(CodingMateApplication.class, args);
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        log.info("applicationReady status{}", Arrays.toString(environment.getActiveProfiles()));
    }

    private static void init() {
        log.info("Spring Server TimeZone : Asia/Seoul");
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
    }
}