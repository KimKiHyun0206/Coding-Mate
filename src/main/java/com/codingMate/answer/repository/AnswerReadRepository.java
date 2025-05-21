package com.codingMate.answer.repository;

import com.codingMate.answer.domain.Answer;
import com.codingMate.answer.domain.vo.LanguageType;
import com.codingMate.answer.dto.response.AnswerListResponse;
import com.codingMate.answer.dto.response.QAnswerListResponse;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.codingMate.answer.domain.QAnswer.answer;

@Repository
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class AnswerReadRepository {
    private final DefaultAnswerRepository answerRepository;
    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    @Transactional(readOnly = true)
    public Answer read(Long answerId) {
        return queryFactory.selectFrom(answer)
                .where(answer.id.eq(answerId))
                .join(answer.programmer)
                .fetchOne();
    }

    @Transactional(readOnly = true)
    public long countProgrammerWroteAnswer(Long programmerId) {
        return queryFactory.select(Wildcard.count)
                .from(answer)
                .where(answer.programmer.id.eq(programmerId))
                .fetchCount();
    }

    @Transactional(readOnly = true)
    public List<AnswerListResponse> readAll(LanguageType languageType, Long backjoonId) {
        return queryFactory.select(new QAnswerListResponse(answer.id, answer.backJoonId, answer.title, answer.programmer.name.name, answer.languageType))
                .from(answer)
                .where(languageType != null ? answer.languageType.eq(languageType) : null)
                .where(backjoonId != null ? answer.backJoonId.eq(backjoonId) : null)
                .join(answer.programmer)
                .fetch();
    }

    @Transactional(readOnly = true)
    public List<AnswerListResponse> readAllByProgrammerId(LanguageType language, Long backjoonId, Long programmerId) {
        return queryFactory.select(new QAnswerListResponse(answer.id, answer.backJoonId, answer.title, answer.programmer.name.name, answer.languageType))
                .from(answer)
                .where(answer.programmer.id.eq(programmerId))
                .where(language != null ? answer.languageType.eq(language) : null)
                .where(backjoonId != null ? answer.backJoonId.eq(backjoonId) : null)
                .join(answer.programmer)
                .fetch();
    }
}
