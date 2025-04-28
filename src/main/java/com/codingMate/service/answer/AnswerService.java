package com.codingMate.service.answer;

import com.codingMate.domain.answer.Answer;
import com.codingMate.domain.programmer.Programmer;
import com.codingMate.dto.request.answer.AnswerCreateDto;
import com.codingMate.dto.request.answer.AnswerUpdateDto;
import com.codingMate.dto.response.answer.AnswerDto;
import com.codingMate.dto.response.answer.AnswerListDto;
import com.codingMate.dto.response.answer.AnswerWithCommentDto;
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
    public List<AnswerDto> readAll() {
        log.info("readAll()");
        return answerRepository.findAll()
                .stream()
                .map(Answer::toDto)
                .toList();
    }

    /**
     * @apiNote 문제 리스트 페이지에서 간단하게 백준 번호와 작성자만 확인할 수 있도록 해주는 서비스
     * @implSpec join문을 사용하여 쿼리가 한 번만 나가게 함
     * @implNote QDto 객체에 fetchJoin을 사용하면 조회가 안 되니 향후 구현에 주의할 것
    * */
    @Transactional(readOnly = true)
    public List<AnswerListDto> readAnswerlist(){
        log.info("readAnswerlist()");
        return queryFactory.select(new QAnswerListDto(answer.id, answer.backJoonId, answer.programmer.name.name))
                .from(answer)
                .join(answer.programmer)
                .fetch()
                .stream().toList();
    }

    @Transactional(readOnly = true)
    public AnswerWithCommentDto answerPageLoadService(@NotNull Long answerId) {
        log.info("answerPageLoadService({})", answerId);
        Answer result = queryFactory.selectFrom(answer)
                .where(answer.id.eq(answerId))
                .join(answer.programmer).fetchJoin()
                .fetchOne();
        if(result == null) throw new NotFoundAnswerException(answerId);
        else return result.toWithCommentDto();
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
    public AnswerDto update(Long programmerId, AnswerUpdateDto dto) {
        long executed = queryFactory.update(answer)
                .where(answer.id.eq(dto.getId()))
                .where(answer.programmer.id.eq(programmerId))
                .set(answer.code, dto.getCode() == null ? null : dto.getCode())
                .set(answer.languageType, dto.getLanguageType() == null ? null : dto.getLanguageType())
                .set(answer.explanation, dto.getExplanation() == null ? null : dto.getExplanation())
                .execute();
        if (executed == 0) throw new NotFoundAnswerException(dto.getId());

        return read(dto.getId());
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