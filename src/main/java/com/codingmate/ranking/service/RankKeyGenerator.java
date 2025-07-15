package com.codingmate.ranking.service;

import com.codingmate.config.properties.RankingProperties;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * Redis에서 Ranking 조회를 위해 필요한 키를 생성해준다
 *
 * @author duskafka
 * */
@Component
public class RankKeyGenerator {

    private final RankingProperties rankingProperties;

    public RankKeyGenerator(RankingProperties rankingProperties) {
        this.rankingProperties = rankingProperties;
    }

    public String getTodayRankingKey() {
        return rankingProperties.key() + ":" + rankingProperties.daily() + ":" + LocalDate.now();
    }
}