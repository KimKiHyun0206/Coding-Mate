package com.codingmate.ranking.service;

import com.codingmate.common.annotation.Explanation;
import com.codingmate.exception.dto.ErrorMessage;
import com.codingmate.exception.exception.ranking.NoRankingException;
import com.codingmate.ranking.dto.SolveCountRankingDto;
import com.codingmate.ranking.repository.RankingReadRepository;
import com.codingmate.redis.RankingRedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Explanation(
        responsibility = "Ranking 정보를 조회하고 업데이트하는 서비스",
        lastReviewed = "2025.07.13"
)
public class RankingService {
    private final RankingReadRepository readRepository;
    private final RankingRedisRepository redisRepository;
    private final RankKeyGenerator rankKeyGenerator;

    @Transactional(readOnly = true)
    public List<SolveCountRankingDto> refreshRanking() {
        List<SolveCountRankingDto> ranking = getRanking();

        saveInRedis(ranking);
        validRankingList(ranking);

        return ranking;
    }

    @Transactional(readOnly = true)
    public List<SolveCountRankingDto> getRanking() {
        List<SolveCountRankingDto> ranking = readRepository.getTop10();

        validRankingList(ranking);

        return ranking;
    }

    public void saveInRedis(List<SolveCountRankingDto> ranking) {
        redisRepository.save(rankKeyGenerator.getTodayRankingKey(), ranking);
    }

    public List<SolveCountRankingDto> getRankingFromRedis() {
        List<SolveCountRankingDto> ranking = redisRepository.getRanking(rankKeyGenerator.getTodayRankingKey());

        validRankingList(ranking);

        return ranking;
    }

    private void validRankingList(List<SolveCountRankingDto> ranking) {
        if(ranking.isEmpty()) {
            log.warn("Redis에서 Ranking을 조회했지만 Ranking이 존재하지 않습니다.");
            throw new NoRankingException(ErrorMessage.NO_RANKING_EXCEPTION, "오늘자 랭킹이 존재하지 않습니다.");
        }
        log.info("Redis에서 Ranking 조회 size: {}", ranking.size());
    }
}