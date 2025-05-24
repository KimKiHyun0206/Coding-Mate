package com.codingMate.answer.repository;

import com.codingMate.answer.domain.Answer;
import com.codingMate.answer.dto.request.AnswerCreateRequest;
import com.codingMate.answer.dto.request.AnswerUpdateRequest;
import com.codingMate.programmer.domain.Programmer;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.codingMate.answer.domain.QAnswer.answer;

@Repository
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class AnswerWriteRepository {
    private final DefaultAnswerRepository answerRepository;
    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

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
