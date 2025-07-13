package com.codingmate.ranking.batch;

import com.codingmate.common.annotation.Explanation;
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
    private IteratorItemReader<SolveCountRankingDto> delegate;

    @Override
    public SolveCountRankingDto read() {
        if (delegate == null) {
            List<SolveCountRankingDto> result = rankingService.getRanking();
            delegate = new IteratorItemReader<>(result);
        }
        return delegate.read();
    }
}