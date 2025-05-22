package com.codingMate.programmer.repository;

import com.codingMate.auth.domain.Authority;
import com.codingMate.programmer.domain.Programmer;
import com.codingMate.programmer.domain.vo.Email;
import com.codingMate.programmer.domain.vo.Name;
import com.codingMate.programmer.dto.request.ProgrammerCreateRequest;
import com.codingMate.programmer.dto.request.ProgrammerUpdateRequest;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static com.codingMate.programmer.domain.QProgrammer.programmer;

@Repository
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ProgrammerWriteRepository {
    private final DefaultProgrammerRepository programmerRepository;
    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    /**
     * @implSpec loginId가 중복되는지 여부 확인 + Programmer 등록으로 쿼리가 두 번 나감
     * */
    @Transactional
    public void create(ProgrammerCreateRequest dto) {
        Authority authority = Authority.builder()
                .authorityName("ROLE_USER")
                .build();

        Programmer entity = Programmer.builder()
                .email(new Email(dto.email()))
                .name(new Name(dto.name()))
                .githubId(dto.githubId())
                .password(dto.password())
                .authority(authority)
                .loginId(dto.loginId())
                .tip("팁이 있다면 공유해주세요")
                .build();

        programmerRepository.save(entity);
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
