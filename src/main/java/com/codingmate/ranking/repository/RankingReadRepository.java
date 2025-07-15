package com.codingmate.ranking.repository;

import com.codingmate.common.annotation.Explanation;
import com.codingmate.ranking.dto.QSolveCountRankingDto;
import com.codingmate.ranking.dto.SolveCountRankingDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.codingmate.answer.domain.QAnswer.answer;
import static com.codingmate.programmer.domain.QProgrammer.programmer;

@Repository
@RequiredArgsConstructor
@Explanation(
        responsibility = "데이터베이스에 저장된 Programmer들 중에서 상위 10명의 풀이 작성자를 읽어온다.",
        detail = "가져오는 값은 programmer_id, programmer_name, count이다.",
        lastReviewed = "2025.07.13"
)
public class RankingReadRepository {
    private final JPAQueryFactory queryFactory;

    public List<SolveCountRankingDto> getTop10(){
        return queryFactory.select(
                    new QSolveCountRankingDto(
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