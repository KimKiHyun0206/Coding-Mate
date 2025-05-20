package com.codingMate.repository.programmer;

import com.codingMate.domain.programmer.Programmer;
import com.codingMate.domain.authority.Authority;
import com.codingMate.domain.programmer.vo.Email;
import com.codingMate.domain.programmer.vo.Name;
import com.codingMate.dto.request.programmer.ProgrammerCreateRequest;
import com.codingMate.dto.request.programmer.ProgrammerUpdateRequest;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.codingMate.domain.authority.QAuthority.authority;
import static com.codingMate.domain.programmer.QProgrammer.programmer;

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

        Programmer entity = dto.toEntity();
        entity.setAuthority(authority);
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
                .set(programmer.name.name, dto.getName() == null ? null : dto.getName())
                .set(programmer.email.email, dto.getEmail() == null ? null : dto.getEmail())
                .set(programmer.githubId, dto.getGithubId() == null ? null : dto.getGithubId())
                .set(programmer.tip, dto.getTip() == null ? null : dto.getTip())
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