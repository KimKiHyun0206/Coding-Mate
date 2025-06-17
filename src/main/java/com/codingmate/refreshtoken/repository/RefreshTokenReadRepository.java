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
        detail = "쿼리 최적화를 위해 Querydsl 사용",
        domain = "RefreshToken",
        lastReviewed = "2025.06.05"
)
public class RefreshTokenReadRepository {
    private final JPAQueryFactory queryFactory;

    @Transactional(readOnly = true)
    public long countRefreshToken(Long userId) {
        Long count = queryFactory
                .select(refreshToken.count())
                .from(refreshToken)
                .where(
                        refreshToken.userId.eq(userId),
                        refreshToken.isRevoked.eq(false)
                )
                .fetchOne();

        return count == null ? 0 : count;
    }

    /**
     * @implNote isRevoked는 null을 반환할 수 있기 때문에 체크 후 리턴
     * */
    @Transactional(readOnly = true)
    public boolean isUsedJti(String jti) {
        Boolean isRevoked = queryFactory.select(refreshToken.isRevoked)
                .from(refreshToken)
                .where(refreshToken.jti.eq(jti))
                .fetchOne();

        return Boolean.TRUE.equals(isRevoked);
    }
}
