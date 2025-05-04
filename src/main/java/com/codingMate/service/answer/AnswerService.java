package com.codingMate.service.answer;

import com.codingMate.domain.answer.Answer;
import com.codingMate.domain.answer.vo.LanguageType;
import com.codingMate.domain.programmer.Programmer;
import com.codingMate.dto.request.answer.AnswerCreateDto;
import com.codingMate.dto.request.answer.AnswerUpdateDto;
import com.codingMate.dto.response.answer.AnswerDto;
import com.codingMate.dto.response.answer.AnswerListDto;
import com.codingMate.dto.response.answer.QAnswerListDto;
import com.codingMate.exception.exception.answer.AnswerAndProgrammerDoNotMatchException;
import com.codingMate.exception.exception.answer.NotFoundAnswerException;
import com.codingMate.exception.exception.programmer.NotFoundProgrammerException;
import com.codingMate.repository.answer.DefaultAnswerRepository;
import com.codingMate.repository.programmer.DefaultProgrammerRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.codingMate.domain.answer.QAnswer.answer;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnswerService {
    private final DefaultAnswerRepository answerRepository;
    private final DefaultProgrammerRepository programmerRepository;
    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    @Transactional
    public AnswerDto create(Long programmerId, AnswerCreateDto answerCreateDto) {
        log.info("create({}, {})", programmerId, answerCreateDto.getCode());
        Programmer programmer = programmerRepository.findById(programmerId).orElseThrow(() -> new NotFoundProgrammerException(programmerId));
        programmer.addAnswer();

        Answer entity = answerCreateDto.toEntity();
        entity.setProgrammer(programmer);
        return answerRepository.save(entity).toDto();
    }

    @Transactional(readOnly = true)
    public AnswerDto read(Long answerId) {
        log.info("read({})", answerId);
        return answerRepository
                .findById(answerId)
                .orElseThrow(() -> new NotFoundAnswerException(answerId))
                .toDto();
    }

    @Transactional(readOnly = true)
    public List<AnswerListDto> readAll(LanguageType languageType, Long backjoonId) {
        log.info("readAll()");
        return queryFactory.select(new QAnswerListDto(answer.id, answer.backJoonId, answer.title, answer.programmer.name.name, answer.languageType))
                .from(answer)
                .where(languageType != null ? answer.languageType.eq(languageType) : null)
                .where(backjoonId != null ? answer.backJoonId.eq(backjoonId) : null)
                .join(answer.programmer)
                .fetch();
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
    public AnswerDto update(Long programmerId, Long answerId, AnswerUpdateDto dto) {
        long executed = queryFactory.update(answer)
                .where(answer.id.eq(answerId))
                .where(answer.programmer.id.eq(programmerId))
                .set(answer.code, dto.getCode() == null ? null : dto.getCode())
                .set(answer.languageType, dto.getLanguageType() == null ? null : dto.getLanguageType())
                .set(answer.explanation, dto.getExplanation() == null ? null : dto.getExplanation())
                .set(answer.title, dto.getTitle() == null ? null : dto.getTitle())
                .execute();
        if (executed == 0) {
            throw new AnswerAndProgrammerDoNotMatchException("해당 풀이를 수정할 권한을 가지고 있지 않습니다");
        }

        return read(answerId);
    }

    @Transactional
    public void delete(@NotNull Long programmerId, @NotNull Long answerId) {
        /*long executedRow = queryFactory
                .delete(answer)
                .where(answer.id.eq(answerId))
                .where(answer.programmer.id.eq(programmerId))
                .execute();
        if (executedRow == 0) {
            throw new NotFoundProgrammerException(programmerId);
        }*/
        Answer answer = answerRepository
                .findById(answerId)
                .orElseThrow(() -> new NotFoundAnswerException(answerId));

        if (answer.getProgrammer().getId().equals(programmerId)) {
            answer.getProgrammer().removeAnswer();
            answerRepository.delete(answer);
        } else {
            throw new AnswerAndProgrammerDoNotMatchException(programmerId, answerId);
        }
    }
}