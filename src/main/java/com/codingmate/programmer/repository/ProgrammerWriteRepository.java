package com.codingmate.programmer.repository;

import com.codingmate.auth.domain.Authority;
import com.codingmate.common.annotation.Explanation;
import com.codingmate.programmer.domain.Programmer;
import com.codingmate.programmer.dto.request.ProgrammerCreateRequest;
import com.codingmate.programmer.dto.request.ProgrammerUpdateRequest;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.codingmate.programmer.domain.QProgrammer.programmer;

@Slf4j
@Repository
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Explanation(
        responsibility = "Programmer Command",
        detail = "쿼리 최적화를 위해 Querydsl 사용",  //클래스 분리 요함
        domain = "Programmer",
        lastReviewed = "2025.06.05"
)
public class ProgrammerWriteRepository {
    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    /**
     * @implSpec loginId가 중복되는지 여부 확인 + Programmer 등록으로 쿼리가 두 번 나감
     * */
    @Transactional
    public Optional<Programmer> create(ProgrammerCreateRequest request, Authority authority) {
        log.info(request.toString());
        var entity = Programmer.toEntity(request, authority);
        em.persist(entity);
        log.info(entity.toString());
        return Optional.of(entity);
    }

    @Transactional
    public long update(Long programmerId, ProgrammerUpdateRequest dto) {
        return queryFactory.update(programmer)
                .where(programmer.id.eq(programmerId))
                .set(programmer.name.name, dto.name() == null ? null : dto.name())
                .set(programmer.email.email, dto.email() == null ? null : dto.email())
                .set(programmer.githubId, dto.githubId() == null ? null : dto.githubId())
                .set(programmer.tip, dto.tip() == null ? null : dto.tip())
                .execute();
    }
}
