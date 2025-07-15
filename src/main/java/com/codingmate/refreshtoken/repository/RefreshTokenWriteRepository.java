package com.codingmate.refreshtoken.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.codingmate.refreshtoken.domain.QRefreshToken.refreshToken;

/**
 * RefreshToken 엔티티를 쓰기 위한 레포지토리
 * <li>쿼리 최적화를 위해 Querydsl을 사용</li>
 *
 * @author duskafka
 * */
@Repository
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshTokenWriteRepository {
    private final JPAQueryFactory queryFactory;

    public long revokeAllToken(String username) {
        return queryFactory.update(refreshToken)
                .where(
                        refreshToken.username.eq(username),
                        refreshToken.isRevoked.eq(false)
                )
                .set(refreshToken.isRevoked, true)
                .execute();
    }
}
