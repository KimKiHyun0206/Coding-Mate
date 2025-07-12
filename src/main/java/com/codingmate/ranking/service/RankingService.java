package com.codingmate.ranking.service;

import com.codingmate.config.properties.RankingProperties;
import com.codingmate.ranking.repository.RankingReadRepository;
import com.codingmate.ranking.dto.RankingReadDto;
import com.codingmate.redis.RankingRedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RankingService {
    private final RankingReadRepository readRepository;
    private final RankingRedisRepository redisRepository;
    private final RankingProperties rankingProperties;

    @Transactional(readOnly = true)
    public List<RankingReadDto> refreshRanking() {
        List<RankingReadDto> ranking = getRanking();
        saveInRedis(ranking);

        return ranking;
    }

    @Transactional(readOnly = true)
    public List<RankingReadDto> getRanking() {
        List<RankingReadDto> top10 = readRepository.getTop10();
        for (RankingReadDto readDto : top10) {
            log.info("RankingDTO {}", readDto.toString());
        }
        return top10;
    }

    public void saveInRedis(List<RankingReadDto> ranking) {
        redisRepository.save(rankingProperties.getKey(), ranking);
    }

    public List<RankingReadDto> getRankingFromRedis() {
        List<RankingReadDto> ranking = redisRepository.getRanking(rankingProperties.getKey());
        log.info("Redis에서 Ranking 조회 size: {}", ranking.size());
        return ranking;
    }
}