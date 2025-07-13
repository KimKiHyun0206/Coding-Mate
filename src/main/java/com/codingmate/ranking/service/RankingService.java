package com.codingmate.ranking.service;

import com.codingmate.config.properties.RankingProperties;
import com.codingmate.exception.dto.ErrorMessage;
import com.codingmate.exception.exception.ranking.NoRankingException;
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
        validRankingList(ranking);

        return ranking;
    }

    @Transactional(readOnly = true)
    public List<RankingReadDto> getRanking() {
        List<RankingReadDto> ranking = readRepository.getTop10();

        validRankingList(ranking);

        return ranking;
    }

    public void saveInRedis(List<RankingReadDto> ranking) {
        redisRepository.save(rankingProperties.getKey(), ranking);
    }

    public List<RankingReadDto> getRankingFromRedis() {
        List<RankingReadDto> ranking = redisRepository.getRanking(rankingProperties.getKey());

        validRankingList(ranking);

        return ranking;
    }

    private void validRankingList(List<RankingReadDto> ranking) {
        if(ranking.isEmpty()) {
            log.warn("Redis에서 Ranking을 조회했지만 Ranking이 존재하지 않습니다.");
            throw new NoRankingException(ErrorMessage.NO_RANKING_EXCEPTION, "오늘자 랭킹이 존재하지 않습니다.");
        }
        log.info("Redis에서 Ranking 조회 size: {}", ranking.size());
    }
}