package com.codingmate.ranking;

import com.codingmate.ranking.dto.QRankingReadDto;
import com.codingmate.ranking.dto.RankingReadDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.codingmate.answer.domain.QAnswer.answer;
import static com.codingmate.programmer.domain.QProgrammer.programmer;

@Repository
@RequiredArgsConstructor
public class RankingReadRepository {
    private final JPAQueryFactory queryFactory;

    public List<RankingReadDto> getTop10(){
        return queryFactory.select(
                    new QRankingReadDto(
                        programmer.id,
                        programmer.name.name,
                        answer.count()
                    )
                )
                .from(answer)
                .join(answer.programmer, programmer)
                .groupBy(programmer.id, programmer.name.name)
                .orderBy(answer.count().desc())
                .limit(10)
                .fetch();
    }
}