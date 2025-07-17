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

    /**
     * 사용자 랭킹을 갱신합니다.
     * <li>데이터베이스에서 새로운 사용자 랭킹을 읽어옵니다.</li>
     * <li>Redis에 저장된 오늘의 랭킹을 수정합니다.</li>
     *
     * @return 새로 읽어온 10명의 랭킹 정보
     * */
    @Transactional(readOnly = true)
    public List<SolveCountRankingDto> refreshRanking() {
        log.debug("[RankingService] refreshRanking()");
        List<SolveCountRankingDto> ranking = getRanking();

        saveInRedis(ranking);

        log.info("[RankingService] 랭킹이 정상적으로 갱신되었습니다.");
        return ranking;
    }

    /**
     * 데이터베이스에서 풀이 갯수 상위 10명의 정보를 읽어옵니다.
     *
     * <li>읽어온 값이 비어있는지 확인합니다.</li>
     * */
    @Transactional(readOnly = true)
    public List<SolveCountRankingDto> getRanking() {
        log.debug("[RankingService] getRanking()");
        List<SolveCountRankingDto> ranking = readRepository.getTop10();

        checkRankingListNotEmpty(ranking, "Database");

        log.info("[RankingService] 데이터베이스에서 랭킹 조회에 성공했습니다.");
        return ranking;
    }

    /**
     * Redis에 매개변수를 저장합니다.
     *
     * @param ranking 풀이 갯수 상위 10명의 정보
     * */
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

    /**
     * Redis에 저장된 풀이 갯수 상위 10명의 정보를 가져옵니다.
     *
     * <li>가져온 List가 빈 값인지 검증합니다.</li>
     * */
    public List<SolveCountRankingDto> getRankingFromRedis() {
        log.debug("[RankingService] getRankingFromRedis()");
        List<SolveCountRankingDto> ranking = redisRepository.getRanking(rankKeyGenerator.getTodayRankingKey());

        checkRankingListNotEmpty(ranking, "Redis");

        log.info("[RankingService] Redis에서 랭킹 조회에 성공했습니다.");
        return ranking;
    }

    /**
     * List가 유효한 값인지 검증하기 위한 메소드.
     *
     * <li>빈 값이라면 예외를 던집니다.</li>
     * <li>어디서 읽어온 리소스인지 로깅합니다.</li>
     *
     * @exception NoRankingException 랭킹이 존재하지 않음을 의미하는 예외
     * */
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