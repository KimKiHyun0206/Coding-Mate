package com.codingmate.ranking.service;

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

/**
 * Ranking 정보를 조회하고 업데이트하는 서비스
 *
 * @author duskafka
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RankingService {
    private final RankingReadRepository readRepository;
    private final RankingRedisRepository redisRepository;
    private final RankKeyGenerator rankKeyGenerator;

    @Transactional(readOnly = true)
    public List<SolveCountRankingDto> refreshRanking() {
        log.debug("[RankingService] refreshRanking()");
        List<SolveCountRankingDto> ranking = getRanking();

        saveInRedis(ranking);

        log.info("[RankingService] 랭킹이 정상적으로 갱신되었습니다.");
        return ranking;
    }

    @Transactional(readOnly = true)
    public List<SolveCountRankingDto> getRanking() {
        log.debug("[RankingService] getRanking()");
        List<SolveCountRankingDto> ranking = readRepository.getTop10();

        checkRankingListNotEmpty(ranking, "Database");

        log.info("[RankingService] 데이터베이스에서 랭킹 조회에 성공했습니다.");
        return ranking;
    }

    public void saveInRedis(List<SolveCountRankingDto> ranking) {
        log.debug("[RankingService] saveInRedis({})", ranking.size());
        try {
            redisRepository.save(rankKeyGenerator.getTodayRankingKey(), ranking);
            log.info("[RankingService] Redis에 랭킹 저장에 성공했습니다: size={}", ranking.size());
        } catch (RedisConnectionFailureException | RedisSystemException e) {
            throw new FailedSaveRankingInRedisException(
                    ErrorMessage.FAILED_SAVE_RANKING_IN_REDIS,
                    "연결 또는 시스템 오류로 Redis에 저장하는 데 실패했습니다"
            );
        } catch (Exception e) {
            throw new FailedSaveRankingInRedisException(
                    ErrorMessage.FAILED_SAVE_RANKING_IN_REDIS,
                    "예상치 못한 오류로 Redis에 저장하는 데 실패했습니다"
            );
        }
    }

    public List<SolveCountRankingDto> getRankingFromRedis() {
        log.debug("[RankingService] getRankingFromRedis()");
        List<SolveCountRankingDto> ranking = redisRepository.getRanking(rankKeyGenerator.getTodayRankingKey());

        checkRankingListNotEmpty(ranking, "Redis");

        log.info("[RankingService] Redis에서 랭킹 조회에 성공했습니다.");
        return ranking;
    }

    private void checkRankingListNotEmpty(List<SolveCountRankingDto> ranking, String source) {
        if (ranking == null || ranking.isEmpty()) {
            throw new NoRankingException(
                    ErrorMessage.NO_RANKING,
                    "오늘자 랭킹이 존재하지 않습니다."
            );
        }
        log.info("{}에서 Ranking 조회 size: {}", source, ranking.size());
    }
}