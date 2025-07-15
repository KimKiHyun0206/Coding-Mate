package com.codingmate.programmer.repository;

import com.codingmate.auth.domain.Authority;
import com.codingmate.programmer.domain.Programmer;
import com.codingmate.programmer.dto.request.ProgrammerCreateRequest;
import com.codingmate.programmer.dto.request.ProgrammerUpdateRequest;
import com.codingmate.programmer.dto.response.ProgrammerResponse;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Set;

import static com.codingmate.programmer.domain.QProgrammer.programmer;

/**
 * 데이터베이스에 Programmer를 쓰기 위한 레포지토리
 *
 * <li>쿼리 최적화를 위해 Querydsl을 사용</li>
 *
 * @author duskafka
 * */
@Slf4j
@Repository
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ProgrammerWriteRepository {
    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    /**
     * @implSpec loginId가 중복되는지 여부 확인 + Programmer 등록으로 쿼리가 두 번 나감
     * @implNote em.persist는 항상 값을 반환하기 때문에 Optional을 사용하지 않는다.
     * */
    public ProgrammerResponse create(ProgrammerCreateRequest request, Set<Authority> authority) {
        var entity = Programmer.toEntity(request, authority);
        em.persist(entity);

        return ProgrammerResponse.of(entity);
    }

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
