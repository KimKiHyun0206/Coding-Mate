package com.codingmate.ranking.dto;

import com.querydsl.core.annotations.QueryProjection;

/**
 * 풀이 상위 10명에 대한 정보를 저장하는 DTO
 *
 * <li>이를 사용하여 {@code List<SolveCountRankingDto>}형태로 Redis에 저장한다</li>
 * <li>programmerId: 사용자를 구분하기 위한 PK</li>
 * <li>name: 사용자의 이름</li>
 * <li>score: 사용자가 작성한 풀이의 수</li>
 *
 * @author duskafka
 * */
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