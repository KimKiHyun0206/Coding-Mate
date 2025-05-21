package com.codingMate.answer.repository;

import com.codingMate.answer.domain.Answer;
import com.codingMate.answer.domain.vo.LanguageType;
import com.codingMate.answer.dto.response.QAnswerListResponse;
import com.codingMate.programmer.domain.Programmer;
import com.codingMate.answer.dto.request.AnswerCreateRequest;
import com.codingMate.answer.dto.request.AnswerUpdateRequest;
import com.codingMate.answer.dto.response.AnswerListResponse;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.codingMate.answer.domain.QAnswer.answer;


@Slf4j
@Service
@RequiredArgsConstructor
public class CustomAnswerRepository {
    private final DefaultAnswerRepository answerRepository;
    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    @Transactional
    public Answer create(Programmer programmer, AnswerCreateRequest answerCreateRequest) {
        Answer entity = answerCreateRequest.toEntity();
        entity.setProgrammer(programmer);

        return answerRepository.save(entity);
    }

    @Transactional(readOnly = true)
    public Answer read(Long answerId) {
        log.info("read({})", answerId);
        return queryFactory.selectFrom(answer)
                .where(answer.id.eq(answerId))
                .join(answer.programmer)
                .fetchOne();
    }

    @Transactional(readOnly = true)
    public long wroteAnswerByProgrammer(Long programmerId) {
        return queryFactory.select(Wildcard.count)
                .from(answer)
                .where(answer.programmer.id.eq(programmerId))
                .fetchCount();
    }

    @Transactional(readOnly = true)
    public List<AnswerListResponse> readAll(LanguageType languageType, Long backjoonId) {
        log.info("readAll()");
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
    public boolean delete(@NotNull Long programmerId, @NotNull Long answerId) {
        /*long executedRow = queryFactory
                .delete(answer)
                .where(answer.id.eq(answerId))
                .where(answer.programmer.id.eq(programmerId))
                .execute();
        if (executedRow == 0) {
            throw new NotFoundProgrammerException(programmerId);
        }*/
        Answer answer = answerRepository
                .findById(answerId)
                .orElse(null);
        if (answer == null) return false;

        if (answer.getProgrammer().getId().equals(programmerId)) {
            answerRepository.delete(answer);
            return true;
        }
        return false;
    }

    @Transactional
    public long deleteByProgrammerId(Long id) {
        return queryFactory.delete(answer)
                .where(answer.programmer.id.eq(id))
                .execute();
    }
}