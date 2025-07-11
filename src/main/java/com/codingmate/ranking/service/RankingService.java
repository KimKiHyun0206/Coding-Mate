package com.codingmate.ranking.service;

import com.codingmate.ranking.RankingReadRepository;
import com.codingmate.ranking.dto.RankingReadDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RankingService {
    private final RankingReadRepository readRepository;

    @Transactional(readOnly = true)
    public List<RankingReadDto> getRanking() {
        return readRepository.getTop10();
    }
}