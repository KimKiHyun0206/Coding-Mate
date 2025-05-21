package com.codingMate.programmer.repository;

import com.codingMate.programmer.domain.Programmer;
import com.codingMate.auth.domain.Authority;
import com.codingMate.programmer.domain.vo.Email;
import com.codingMate.programmer.domain.vo.Name;
import com.codingMate.programmer.dto.request.ProgrammerCreateRequest;
import com.codingMate.programmer.dto.request.ProgrammerUpdateRequest;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.codingMate.auth.domain.QAuthority.authority;
import static com.codingMate.programmer.domain.QProgrammer.programmer;


@Slf4j
@Service
@RequiredArgsConstructor
public class CustomProgrammerRepository {
    private final DefaultProgrammerRepository programmerRepository;
    private final JPAQueryFactory queryFactory;
    private final EntityManager em;


    /**
     * @implSpec loginId가 중복되는지 여부 확인 + Programmer 등록으로 쿼리가 두 번 나감
     * */
    @Transactional
    public Programmer create(ProgrammerCreateRequest dto) {
        log.info("create({})", dto);
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

        return programmerRepository.save(entity);
    }

    /**
     * @implSpec * exists를 사용하지 않음으로써 성능 개선
     *           * limit 1을 사용해서 1개를 찾으면 쿼리를 중단하도록 함
     * */
    @Transactional(readOnly = true)
    public boolean isExistLoginId(String loginId) {
        log.info("isExistLoginId({})", loginId);
        return queryFactory.selectOne()
                .from(programmer)
                .where(programmer.loginId.eq(loginId))
                .limit(1)
                .fetchFirst() != null;
    }

    @Transactional(readOnly = true)
    public Programmer read(Long id) {
        log.info("read({})", id);
        return programmerRepository
                .findById(id)
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public Programmer readByLoginId(String loginId) {
        log.info("readByLoginId({})", loginId);
        return queryFactory.selectFrom(programmer)
                .where(programmer.loginId.eq(loginId))
                .leftJoin(programmer.authority, authority)
                .fetchOne();
    }

    @Transactional(readOnly = true)
    public List<Programmer> readAll() {
        log.info("readAll()");
        return programmerRepository
                .findAll();
    }

    @Transactional(readOnly = true)
    public Long readIdByUserName(String userName) {
        log.info("readIdByUserName({})", userName);
        return queryFactory.select(programmer.id)
                .from(programmer)
                .where(programmer.loginId.eq(userName))
                .fetchOne();
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

    @Transactional
    public boolean delete(Long id) {
        log.info("remove({})", id);
        if (programmerRepository.existsById(id)) {
            programmerRepository.deleteById(id);
            return true;
        }
        return false;
    }
}