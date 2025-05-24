package com.codingMate.programmer.repository;

import com.codingMate.programmer.domain.Programmer;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.codingMate.auth.domain.QAuthority.authority;
import static com.codingMate.programmer.domain.QProgrammer.programmer;

@Repository
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ProgrammerReadRepository {

    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    /**
     * @implSpec * exists를 사용하지 않음으로써 성능 개선
     *           * limit 1을 사용해서 1개를 찾으면 쿼리를 중단하도록 함
     * */
    @Transactional(readOnly = true)
    public boolean isExistLoginId(String loginId) {
        return queryFactory.selectOne()
                .from(programmer)
                .where(programmer.loginId.eq(loginId))
                .limit(1)
                .fetchFirst() != null;
    }

    @Transactional(readOnly = true)
    public Optional<Programmer> readByLoginId(String loginId) {
        return Optional.ofNullable(
                queryFactory.selectFrom(programmer)
                        .where(programmer.loginId.eq(loginId))
                        .leftJoin(programmer.authority, authority)
                        .fetchOne()
        );
    }
}
