package com.codingMate.service.answer;

import com.codingMate.domain.answer.Answer;
import com.codingMate.domain.programmer.Programmer;
import com.codingMate.dto.request.answer.AnswerCreateDto;
import com.codingMate.dto.request.answer.AnswerUpdateDto;
import com.codingMate.dto.response.answer.AnswerDto;
import com.codingMate.exception.exception.answer.NotFoundAnswerException;
import com.codingMate.exception.exception.programmer.NotFoundProgrammerException;
import com.codingMate.repository.answer.DefaultAnswerRepository;
import com.codingMate.repository.programmer.DefaultProgrammerRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.codingMate.domain.answer.QAnswer.answer;

@Slf4j
@Service
public class AnswerService {
    private final DefaultAnswerRepository answerRepository;
    private final DefaultProgrammerRepository programmerRepository;
    private final JPAQueryFactory queryFactory;
    private final EntityManager em;
    private final DefaultAnswerRepository defaultAnswerRepository;

    public AnswerService(EntityManager em, DefaultAnswerRepository answerRepository, DefaultProgrammerRepository programmerRepository, DefaultAnswerRepository defaultAnswerRepository) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
        this.answerRepository = answerRepository;
        this.programmerRepository = programmerRepository;
        this.defaultAnswerRepository = defaultAnswerRepository;
    }

    @Transactional
    public AnswerDto create(Long programmerId, AnswerCreateDto answerCreateDto) {
        log.info("[SYSTEM] com.codingMate.service.answer.AnswerService.create({}, {})",programmerId, answerCreateDto.hashCode());
        Programmer programmer = programmerRepository
                .findById(programmerId)
                .orElseThrow(() -> new NotFoundProgrammerException(programmerId));

        Answer entity = answerCreateDto.toEntity();
        entity.setProgrammer(programmer);

        Answer saved = answerRepository.save(entity);
        programmer.getAnswers().add(entity);

        return saved.toDto();
    }

    @Transactional(readOnly = true)
    public AnswerDto read(Long answerId) {
        log.info("[SYSTEM] com.codingMate.service.answer.AnswerService.read({})",answerId);
        return answerRepository
                .findById(answerId)
                .orElseThrow(() -> new NotFoundAnswerException(answerId))
                .toDto();
    }

    @Transactional(readOnly = true)
    public List<AnswerDto> readAll() {
        log.info("[SYSTEM] com.codingMate.service.answer.AnswerService.readAll()");
        return answerRepository.findAll()
                .stream()
                .map(Answer::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AnswerDto> readAllByProgrammerId(Long programmerId) {
        return answerRepository.readAnswersByProgrammerId(programmerId)
                .stream()
                .map(Answer::toDto)
                .toList();
        /*return queryFactory.selectFrom(answer)
                .where(answer.programmer.id.eq(programmerId))
                .fetch()
                .stream()
                .map(Answer::toDto)
                .toList();*/
    }

    @Transactional
    public AnswerDto update(Long programmerId, AnswerUpdateDto dto){
        long executed = queryFactory.update(answer)
                .where(answer.id.eq(dto.getId()))
                .where(answer.programmer.id.eq(programmerId))
                .set(answer.code, dto.getCode() == null ? null : dto.getCode())
                .set(answer.languageType, dto.getLanguageType() == null ? null : dto.getLanguageType())
                .set(answer.explanation, dto.getExplanation() == null ? null : dto.getExplanation())
                .execute();
        if(executed == 0) throw new NotFoundAnswerException(dto.getId());

        return read(dto.getId());
    }

    @Transactional
    public void delete(Long programmerId, Long answerId) {
        if (programmerId == null || answerId == null) {
            throw new NotFoundProgrammerException(programmerId);
        }else {
            long executedRow = queryFactory.delete(answer)
                    .where(answer.id.eq(answerId))
                    .where(answer.programmer.id.eq(programmerId))
                    .execute();
            if(executedRow == 0){
                throw new NotFoundProgrammerException(programmerId);
            }
        }
    }
}