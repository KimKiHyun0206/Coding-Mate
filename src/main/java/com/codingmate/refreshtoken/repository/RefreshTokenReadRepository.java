package com.codingmate.refreshtoken.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.codingmate.refreshtoken.domain.QRefreshToken.refreshToken;

/**
 * RefreshToken 엔티티를 읽어오기 위한 레포지토리
 *
 * <li>쿼리 최적화를 위해 Querydsl을 사용함</li>
 * <li>반환값은 간단하게 long, boolean을 사용함</li>
 *
 * @author duskafka
 * */
@Repository
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshTokenReadRepository {
    private final JPAQueryFactory queryFactory;

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
    public boolean isUsedJti(String jti) {
        Boolean isRevoked = queryFactory.select(refreshToken.isRevoked)
                .from(refreshToken)
                .where(refreshToken.jti.eq(jti))
                .fetchOne();

        return Boolean.TRUE.equals(isRevoked);
    }
}
