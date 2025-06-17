package com.codingmate.answer.repository;

import com.codingmate.answer.domain.Answer;
import com.codingmate.answer.domain.vo.LanguageType;
import com.codingmate.answer.dto.response.AnswerListResponse;
import com.codingmate.answer.dto.response.QAnswerListResponse;
import com.codingmate.common.annotation.Explanation;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.codingmate.answer.domain.QAnswer.answer;

@Repository
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Explanation(
        responsibility = "Answer Query",
        detail = "성능 최적화를 위해 Querydsl을 사용",
        domain = "Answer",
        lastReviewed = "2025.06.05"
)
public class AnswerReadRepository {
    private final JPAQueryFactory queryFactory;

    @Transactional(readOnly = true)
    public Optional<Answer> read(Long answerId) {
        return Optional.ofNullable(
                queryFactory.selectFrom(answer)
                        .where(answer.id.eq(answerId))
                        .join(answer.programmer)
                        .fetchJoin()
                        .fetchOne()
        );
    }

    /**
     * @implNote fetchOne은 null을 반환할 수 있다.
     * @implSpec answer.count를 사용해서 숫자를 가져오도록 한다.
     * */
    @Transactional(readOnly = true)
    public long countProgrammerWroteAnswer(Long programmerId) {
        Long count = queryFactory.select(answer.count())
                .from(answer)
                .where(answer.programmer.id.eq(programmerId))
                .fetchOne();
        return count != null ? count : 0L;
    }

    @Transactional(readOnly = true)
    public Page<AnswerListResponse> readAll(LanguageType languageType, Long backjoonId, Pageable pageable) {

        // 1. 실제 데이터를 조회하는 쿼리 실행
        List<AnswerListResponse> content = queryFactory
                .select(new QAnswerListResponse(answer.id, answer.backJoonId, answer.title, answer.programmer.name.name, answer.languageType))
                .from(answer)
                .join(answer.programmer) // join을 .from() 바로 아래에 두는 것이 일반적
                .fetchJoin()
                .where(
                        languageTypeEq(languageType), // 조건 메서드 사용
                        backjoonIdEq(backjoonId)     // 조건 메서드 사용
                )
                .orderBy(answer.createdAt.desc()) // 정렬 추가 (일반적으로 createdAt 기준으로 최신순 정렬)
                .offset(pageable.getOffset()) // 페이징 시작 지점
                .limit(pageable.getPageSize()) // 한 페이지에 가져올 데이터 수
                .fetch(); // 결과 리스트를 가져옴

        // 2. 전체 개수를 조회하는 카운트 쿼리 정의
        // count()는 SQL의 count(*)와 유사하게 전체 개수를 계산합니다.
        JPAQuery<Long> countQuery = queryFactory
                .select(answer.count())
                .from(answer)
                .join(answer.programmer)
                .where(
                        languageTypeEq(languageType),
                        backjoonIdEq(backjoonId)
                );

        // 3. PageableExecutionUtils.getPage()를 사용하여 Page 객체 생성
        // content: 현재 페이지의 데이터 리스트
        // pageable: 클라이언트로부터 받은 Pageable 객체
        // countQuery::fetchOne: 전체 개수를 조회하는 람다 함수 (필요할 때만 실행되어 최적화)
        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private BooleanExpression languageTypeEq(LanguageType languageType) {
        return languageType != null ? answer.languageType.eq(languageType) : null;
    }

    private BooleanExpression backjoonIdEq(Long backjoonId) {
        return backjoonId != null ? answer.backJoonId.eq(backjoonId) : null;
    }

    @Transactional(readOnly = true)
    public Page<AnswerListResponse> readAllByProgrammerId(LanguageType languageType, Long backjoonId, Long programmerId, Pageable pageable) {
        List<AnswerListResponse> content = queryFactory.select(new QAnswerListResponse(answer.id, answer.backJoonId, answer.title, answer.programmer.name.name, answer.languageType))
                .from(answer)
                .join(answer.programmer)
                .fetchJoin()
                .where(
                        answer.programmer.id.eq(programmerId),
                        languageTypeEq(languageType),
                        backjoonIdEq(backjoonId)
                )
                .orderBy(answer.createdAt.desc())   //정렬 추가
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(answer.count())
                .from(answer)
                .join(answer.programmer)
                .where(
                        languageTypeEq(languageType),
                        backjoonIdEq(backjoonId)
                );

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }
}
