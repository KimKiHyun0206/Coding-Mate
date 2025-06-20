package com.codingmate.programmer.repository;

import com.codingmate.common.annotation.Explanation;
import com.codingmate.programmer.domain.Programmer;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.codingmate.auth.domain.QAuthority.authority;
import static com.codingmate.programmer.domain.QProgrammer.programmer;

@Slf4j
@Repository
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Explanation(
        responsibility = "Programmer Query",
        detail = "쿼리 최적화를 위해 Querydsl 사용",  //클래스 분리 요함
        domain = "Programmer",
        lastReviewed = "2025.06.05"
)
public class ProgrammerReadRepository {
    private final JPAQueryFactory queryFactory;

    /**
     * @implSpec * exists를 사용하지 않음으로써 성능 개선
     *           * limit 1을 사용해서 1개를 찾으면 쿼리를 중단하도록 함
     * @implSpec CREATE INDEX idx_programmer_login_id ON programmer(login_id); 인덱스를 만들어둠
     * */
    public boolean isExistLoginId(String loginId) {
        return queryFactory.selectOne()
                .from(programmer)
                .where(programmer.loginId.eq(loginId))
                .limit(1)
                .fetchFirst() != null;
    }

    /**
     * @implSpec fetchJoin으로 지연로딩 방지
     * */
    public Optional<Programmer> readByLoginId(String loginId) {
        return Optional.ofNullable(
                queryFactory.selectFrom(programmer)
                        .where(programmer.loginId.eq(loginId))
                        .leftJoin(programmer.authority, authority)
                        .fetchJoin()
                        .fetchOne()
        );
    }

    /**
     * @implSpec fetchJoin으로 지연로딩 방지
     * */
    public Optional<String> readProgrammerRole (Long programmerId) {
        Programmer result = queryFactory.selectFrom(programmer)
                .where(programmer.id.eq(programmerId))
                .leftJoin(programmer.authority, authority)
                .fetchJoin()
                .fetchOne();
        return result.getAuthority().getAuthorityName().describeConstable();
    }

    //TODO 로그인 아이디와 비밀번호를 바꾸는 로직은 DTO 에서는 null-safe patch update DTO을 사용하도록 함.
}
