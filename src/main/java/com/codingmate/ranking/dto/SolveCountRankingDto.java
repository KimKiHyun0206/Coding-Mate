package com.codingmate.ranking.dto;

import com.querydsl.core.annotations.QueryProjection;

public record SolveCountRankingDto(
        Long programmerId,
        String name,
        Long score
) {
    @QueryProjection
    public SolveCountRankingDto(Long programmerId, String name, Long score) {
        this.programmerId = programmerId;
        this.name = name;
        this.score = score;
    }
}