package com.codingmate.answer.repository;

import com.codingmate.answer.dto.request.AnswerUpdateRequest;
import com.codingmate.common.annotation.Explanation;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static com.codingmate.answer.domain.QAnswer.answer;

@Repository
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Explanation(
        responsibility = "Answer Command",
        detail = "Querydsl을 사용",
        domain = "Answer",
        lastReviewed = "2025.06.05"
)
public class AnswerWriteRepository {
    private final JPAQueryFactory queryFactory;

    public long update(Long programmerId, Long answerId, AnswerUpdateRequest dto) {
        return queryFactory.update(answer)
                .where(
                        answer.id.eq(answerId),
                        answer.programmer.id.eq(programmerId)
                )
                .set(answer.code, dto.code() == null ? null : dto.code())
                .set(answer.languageType, dto.languageType() == null ? null : dto.languageType())
                .set(answer.explanation, dto.explanation() == null ? null : dto.explanation())
                .set(answer.title, dto.title() == null ? null : dto.title())
                .execute();
    }

    public long deleteByProgrammerId(Long id) {
        return queryFactory.delete(answer)
                .where(answer.programmer.id.eq(id))
                .execute();
    }
}
