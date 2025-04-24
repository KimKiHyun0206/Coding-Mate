package com.codingMate.service.programmer;

import com.codingMate.domain.programmer.Programmer;
import com.codingMate.domain.programmer.vo.Email;
import com.codingMate.domain.programmer.vo.Name;
import com.codingMate.domain.tip.Tip;
import com.codingMate.dto.request.programmer.ProgrammerCreateDto;
import com.codingMate.dto.request.programmer.ProgrammerUpdateDto;
import com.codingMate.dto.response.programmer.ProgrammerDto;
import com.codingMate.exception.exception.programmer.NotFoundProgrammerException;
import com.codingMate.repository.programmer.DefaultProgrammerRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.codingMate.domain.programmer.QProgrammer.programmer;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProgrammerService {
    private final DefaultProgrammerRepository programmerRepository;
    private final JPAQueryFactory queryFactory;
    private final EntityManager em;


    @Transactional
    public ProgrammerDto create(ProgrammerCreateDto dto) {
        log.info("[SYSTEM] com.codingMate.service.programmer.ProgrammerService.create({})", dto.getName());
        Programmer entity = dto.toEntity();
        Tip tip = new Tip("아무 내용이 없습니다. 나만의 팁이 있다면 공유해주세요");
        entity.setTip(tip);
        return programmerRepository.save(entity).toDto();
    }

    @Transactional(readOnly = true)
    public ProgrammerDto read(Long id) {
        log.info("[SYSTEM] com.codingMate.service.programmer.ProgrammerService.read({})", id);
        return programmerRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundProgrammerException(id))
                .toDto();
    }

    @Transactional(readOnly = true)
    public List<ProgrammerDto> readAll() {
        log.info("[SYSTEM] com.codingMate.service.programmer.ProgrammerService.readAll()");
        return programmerRepository
                .findAll()
                .stream()
                .map(Programmer::toDto)
                .toList();
    }

    @Transactional
    public ProgrammerDto update(Long programmerId, ProgrammerUpdateDto dto) {
        long execute = queryFactory.update(programmer)
                .where(programmer.id.eq(programmerId))
                .set(programmer.loginId, dto.getLoginId() == null ? null : dto.getLoginId())
                .set(programmer.email, dto.getEmail() == null ? null : new Email(dto.getEmail()))
                .set(programmer.name, dto.getName() == null ? null : new Name(dto.getName()))
                .set(programmer.password, dto.getPassword() == null ? null : dto.getPassword())
                .execute();

        log.info("[SYSTEM] com.codingMate.service.programmer.ProgrammerService.update({}) executed {}", programmerId, execute);

        if(execute == 0) {
            throw new NotFoundProgrammerException(programmerId);
        }
        return read(programmerId);
    }

    @Transactional
    public boolean delete(Long id) {
        log.info("[SYSTEM] com.codingMate.service.programmer.ProgrammerService.remove({})",id);
        long executed = queryFactory.delete(programmer)
                .where(programmer.id.eq(id))
                .execute();
        if(executed == 0) {
            throw new NotFoundProgrammerException(id);
        }
        return true;
    }
}