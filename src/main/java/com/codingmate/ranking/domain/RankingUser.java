package com.codingmate.ranking.domain;

import com.codingmate.ranking.dto.RankingReadDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class RankingUser {
    private Long programmerId;
    private String name;
    private Long score;

    public static RankingUser createRankingUser(RankingReadDto rankingReadDto) {
        return RankingUser.builder()
                .programmerId(rankingReadDto.programmerId())
                .name(rankingReadDto.name())
                .score(rankingReadDto.score())
                .build();

    }
}