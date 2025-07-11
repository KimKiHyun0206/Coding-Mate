package com.codingmate.ranking.batch;

import com.codingmate.ranking.dto.RankingReadDto;
import com.codingmate.ranking.service.RankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.support.IteratorItemReader;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RankReader implements ItemReader<RankingReadDto> {

    private final RankingService rankingService;
    private boolean readOnce = false;

    @Override
    public RankingReadDto read() throws Exception {
        if (readOnce) return null;

        List<RankingReadDto> result = rankingService.getRanking();

        readOnce = true;
        return new IteratorItemReader<>(result).read();
    }
}
