package com.codingmate.ranking.service;

import com.codingmate.config.properties.RankingProperties;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * Redis에서 Ranking 조회를 위해 필요한 키를 생성해준다
 *
 * @author duskafka
 */
@Component
public class RankKeyGenerator {

    private final String key;
    private final String daily;

    public RankKeyGenerator(RankingProperties rankingProperties) {
        this.key = rankingProperties.key();
        this.daily = rankingProperties.daily();
    }

    /**
     * ranking:daily:2025-01-01 형식으로 키를 생성해줍니다.
     *
     * @return 생성된 키
     */
    public String getTodayRankingKey() {
        return key + ":" + daily + ":" + LocalDate.now();
    }
}