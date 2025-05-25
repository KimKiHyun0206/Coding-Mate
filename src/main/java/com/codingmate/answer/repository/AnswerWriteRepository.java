package com.codingmate.answer.repository;

import com.codingmate.answer.dto.request.AnswerUpdateRequest;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static com.codingmate.answer.domain.QAnswer.answer;

@Repository
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class AnswerWriteRepository {
    private final JPAQueryFactory queryFactory;

    @Transactional
    public long update(Long programmerId, Long answerId, AnswerUpdateRequest dto) {
        return queryFactory.update(answer)
                .where(answer.id.eq(answerId))
                .where(answer.programmer.id.eq(programmerId))
                .set(answer.code, dto.code() == null ? null : dto.code())
                .set(answer.languageType, dto.languageType() == null ? null : dto.languageType())
                .set(answer.explanation, dto.explanation() == null ? null : dto.explanation())
                .set(answer.title, dto.title() == null ? null : dto.title())
                .execute();
    }

    @Transactional
    public long deleteByProgrammerId(Long id) {
        return queryFactory.delete(answer)
                .where(answer.programmer.id.eq(id))
                .execute();
    }
}
