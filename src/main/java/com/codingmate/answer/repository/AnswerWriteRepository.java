package com.codingmate.answer.repository;

import com.codingmate.answer.dto.request.AnswerUpdateRequest;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.codingmate.answer.domain.QAnswer.answer;

/**
 * 데이터베이스에 Answer를 저장하거나 수정하기 위한 레포지토리
 *
 * <li>Querydsl을 사용하여 성능 최적화</li>
 * <li>리턴값은 엔티티를 리턴하지 않고 변경 사항을 알 수 있는 정보를 리턴한다</li>
 *
 * @author duskafka
 * */
@Repository
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class AnswerWriteRepository {
    private final JPAQueryFactory queryFactory;

    public long update(Long answerId, AnswerUpdateRequest dto) {
        return queryFactory.update(answer)
                .where(answer.id.eq(answerId))
                .set(answer.code, dto.code() == null ? null : dto.code())
                .set(answer.languageType, dto.languageType() == null ? null : dto.languageType())
                .set(answer.explanation, dto.explanation() == null ? null : dto.explanation())
                .set(answer.title, dto.title() == null ? null : dto.title())
                .execute();
    }

    public long deleteByLoginId(String loginId) {
        return queryFactory.delete(answer)
                .where(answer.programmer.loginId.eq(loginId))
                .execute();
    }
}
