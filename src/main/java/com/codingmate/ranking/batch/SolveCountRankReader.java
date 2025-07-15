package com.codingmate.ranking.batch;

import com.codingmate.common.annotation.Explanation;
import com.codingmate.exception.dto.ErrorMessage;
import com.codingmate.exception.exception.ranking.NoRankingException;
import com.codingmate.ranking.dto.SolveCountRankingDto;
import com.codingmate.ranking.service.RankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.support.IteratorItemReader;

import java.util.List;

@RequiredArgsConstructor
@Explanation(
        responsibility = "데이터베이스에서 사용자 정보를 가지고 List<RankingReadDto>를 만든다.",
        detail = "상위 10명의 데이터만 가져와서 List를 만든다",
        lastReviewed = "2025.07.13"
)
public class SolveCountRankReader implements ItemReader<SolveCountRankingDto> {

    private final RankingService rankingService;
    private volatile IteratorItemReader<SolveCountRankingDto> delegate; // 멀티 스레드 환경에서 안전을 위해 volatile 사용

    @Override
    public SolveCountRankingDto read() {

        synchronized (this) {
            if (delegate == null) {
                try {
                    List<SolveCountRankingDto> result = rankingService.getRanking();
                    if (result == null || result.isEmpty()) {
                        return null;
                    }
                    delegate = new IteratorItemReader<>(result.iterator());
                } catch (Exception e) {
                    throw new NoRankingException(ErrorMessage.NO_RANKING_EXCEPTION, "Reader에서 Ranking 조회에 실패했습니다");
                }
            }
        }

        return delegate.read();
    }
}