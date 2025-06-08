package com.codingmate.refreshtoken.repository;

import com.codingmate.common.annotation.Explanation;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import static com.codingmate.refreshtoken.domain.QRefreshToken.refreshToken;

@Repository
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Explanation(
        responsibility = "RefreshToken Query",
        detail = "쿼리 최적화를 위해 Querydsl 사용",  //클래스 분리 요함
        domain = "RefreshToken",
        lastReviewed = "2025.06.05"
)
public class RefreshTokenReadRepository {
    private final JPAQueryFactory queryFactory;

    @Transactional(readOnly = true)
    public Long countRefreshToken(Long userId) {
        // select(refreshToken.count())를 사용하여 COUNT(*) 쿼리를 직접 생성
        // fetchOne()을 사용하여 단일 Long 값을 가져옵니다.
        Long count = queryFactory
                .select(refreshToken.count())
                .from(refreshToken)
                .where(refreshToken.userId.eq(userId))
                .where(refreshToken.isRevoked.eq(false))
                .fetchOne();

        return count == null ? 0 : count;
    }

    @Transactional(readOnly = true)
    public Boolean isUsedJti(String jti) {
        return queryFactory.select(refreshToken.isRevoked)
                .from(refreshToken)
                .where(refreshToken.jti.eq(jti))
                .fetchOne();
    }
}
