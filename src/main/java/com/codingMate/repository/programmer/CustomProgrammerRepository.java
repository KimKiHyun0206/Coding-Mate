package com.codingMate.repository.programmer;

import com.codingMate.domain.programmer.Programmer;
import com.codingMate.domain.authority.Authority;
import com.codingMate.domain.programmer.vo.Email;
import com.codingMate.domain.programmer.vo.Name;
import com.codingMate.dto.request.programmer.ProgrammerCreateRequest;
import com.codingMate.dto.request.programmer.ProgrammerUpdateRequest;
import com.codingMate.exception.exception.jwt.UnMatchedAuthException;
import com.codingMate.exception.exception.programmer.DuplicateProgrammerLoginIdException;
import com.codingMate.exception.exception.programmer.NotFoundProgrammerException;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

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
        entity.setAuthorities(Collections.singleton(authority));
        return programmerRepository.save(entity);
    }

    @Transactional(readOnly = true)
    public boolean isExistLoginId(String loginId) {
        log.info("isExistLoginId({})", loginId);
        return programmerRepository.existsByLoginId(loginId);
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
                .leftJoin(programmer.authorities, authority)
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
    public Programmer update(Long programmerId, ProgrammerUpdateRequest dto) {
        Programmer findById = programmerRepository.findById(programmerId).orElse(null);
        if (findById == null) return null;

        findById.setName(dto.getName() == null ? findById.getName() : new Name(dto.getName()));
        findById.setEmail(dto.getEmail() == null ? findById.getEmail() : new Email(dto.getEmail()));
        findById.setTip(dto.getTip() == null ? findById.getTip() : dto.getTip());
        findById.setGithubId(dto.getGithubId() == null ? findById.getGithubId() : dto.getGithubId());
        return findById;
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