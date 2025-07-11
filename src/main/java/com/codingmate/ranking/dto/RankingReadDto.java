package com.codingmate.ranking.dto;

import com.querydsl.core.annotations.QueryProjection;

public record RankingReadDto(
        Long programmerId,
        String name,
        Long score
) {
    @QueryProjection
    public RankingReadDto(Long programmerId, String name, Long score) {
        this.programmerId = programmerId;
        this.name = name;
        this.score = score;
    }
}