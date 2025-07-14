package com.codingmate.ranking.service;

import com.codingmate.common.annotation.Explanation;
import com.codingmate.exception.dto.ErrorMessage;
import com.codingmate.exception.exception.ranking.NoRankingException;
import com.codingmate.exception.exception.redis.FailedSaveRankingInRedisException;
import com.codingmate.ranking.dto.SolveCountRankingDto;
import com.codingmate.ranking.repository.RankingReadRepository;
import com.codingmate.redis.RankingRedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.RedisSystemException;
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

        return ranking;
    }

    @Transactional(readOnly = true)
    public List<SolveCountRankingDto> getRanking() {
        List<SolveCountRankingDto> ranking = readRepository.getTop10();

        checkRankingListNotEmpty(ranking, "Database");

        return ranking;
    }

    public void saveInRedis(List<SolveCountRankingDto> ranking) {
        try {
            redisRepository.save(rankKeyGenerator.getTodayRankingKey(), ranking);
        } catch (RedisConnectionFailureException | RedisSystemException e) {
            log.warn("Redis 연결 또는 시스템 오류로 저장 실패", e);
            throw new FailedSaveRankingInRedisException(
                    ErrorMessage.FAILED_SAVE_RANKING_IN_REDIS,
                    "연결 또는 시스템 오류로 Redis에 저장하는 데 실패했습니다"
            );
        } catch (Exception e) {
            log.error("예상치 못한 Redis 저장 오류", e);
            throw new FailedSaveRankingInRedisException(
                    ErrorMessage.FAILED_SAVE_RANKING_IN_REDIS,
                    "예상치 못한 오류로 Redis에 저장하는 데 실패했습니다"
            );
        }
    }

    public List<SolveCountRankingDto> getRankingFromRedis() {
        List<SolveCountRankingDto> ranking = redisRepository.getRanking(rankKeyGenerator.getTodayRankingKey());

        checkRankingListNotEmpty(ranking, "Redis");

        return ranking;
    }

    private void checkRankingListNotEmpty(List<SolveCountRankingDto> ranking, String source) {
        if (ranking == null || ranking.isEmpty()) {
            log.warn("{}에서 Ranking을 조회했지만 Ranking이 존재하지 않습니다.", source);
            throw new NoRankingException(ErrorMessage.NO_RANKING_EXCEPTION, "오늘자 랭킹이 존재하지 않습니다.");
        }
        log.info("{}에서 Ranking 조회 size: {}", source, ranking.size());
    }
}