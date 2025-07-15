package com.codingmate.ranking.service;

import com.codingmate.config.properties.RankingProperties;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

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
