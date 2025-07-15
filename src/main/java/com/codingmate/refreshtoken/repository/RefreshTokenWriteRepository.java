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
        responsibility = "RefreshToken Command",
        detail = "쿼리 최적화를 위해 Querydsl 사용",  //클래스 분리 요함
        domain = "RefreshToken",
        lastReviewed = "2025.06.07"
)
public class RefreshTokenWriteRepository {
    private final JPAQueryFactory queryFactory;

    public long revokeAllToken(Long userId) {
        return queryFactory.update(refreshToken)
                .where(
                        refreshToken.userId.eq(userId),
                        refreshToken.isRevoked.eq(false)
                )
                .set(refreshToken.isRevoked, true)
                .execute();
    }
}
