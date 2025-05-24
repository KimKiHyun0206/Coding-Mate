package com.codingMate.programmer.repository;

import com.codingMate.programmer.domain.Programmer;
import com.codingMate.programmer.dto.request.ProgrammerCreateRequest;
import com.codingMate.programmer.dto.request.ProgrammerUpdateRequest;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.codingMate.programmer.domain.QProgrammer.programmer;

@Repository
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ProgrammerWriteRepository {
    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    /**
     * @implSpec loginId가 중복되는지 여부 확인 + Programmer 등록으로 쿼리가 두 번 나감
     * */
    @Transactional
    public Optional<Programmer> create(ProgrammerCreateRequest request) {
        var entity = Programmer.toEntity(request);
        em.persist(entity);
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
