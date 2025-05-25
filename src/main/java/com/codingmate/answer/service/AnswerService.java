package com.codingmate.answer.service;

import com.codingmate.answer.domain.Answer;
import com.codingmate.answer.domain.vo.LanguageType;
import com.codingmate.answer.repository.AnswerReadRepository;
import com.codingmate.answer.repository.AnswerWriteRepository;
import com.codingmate.exception.dto.ErrorMessage;
import com.codingmate.answer.dto.request.AnswerCreateRequest;
import com.codingmate.answer.dto.request.AnswerUpdateRequest;
import com.codingmate.answer.dto.response.AnswerCreateResponse;
import com.codingmate.answer.dto.response.AnswerListResponse;
import com.codingmate.answer.dto.response.AnswerPageResponse;
import com.codingmate.exception.exception.answer.AnswerAndProgrammerDoNotMatchException;
import com.codingmate.exception.exception.answer.NotFoundAnswerException;
import com.codingmate.exception.exception.programmer.NotFoundProgrammerException;
import com.codingmate.answer.repository.DefaultAnswerRepository;
import com.codingmate.programmer.repository.DefaultProgrammerRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class AnswerService {
    private final AnswerReadRepository readRepository;
    private final AnswerWriteRepository writeRepository;
    private final DefaultAnswerRepository defaultAnswerRepository;
    private final DefaultProgrammerRepository defaultProgrammerRepository;

    @Transactional
    public AnswerCreateResponse create(Long programmerId, AnswerCreateRequest request) {
        var writer = defaultProgrammerRepository.findById(programmerId)
                .orElseThrow(() -> new NotFoundProgrammerException(
                        ErrorMessage.NOT_FOUND_PROGRAMMER_EXCEPTION,
                        String.format("Answer를 생성하던 중 %d로 Programmer를 조회하지 못했습니다.", programmerId))
                );

        var createdResult = defaultAnswerRepository.save(Answer.toEntity(request, writer));

        return new AnswerCreateResponse(createdResult.getId());
    }

    @Transactional(readOnly = true)
    public AnswerPageResponse read(Long answerId, Long programmerId) {
        var answer = readRepository.read(answerId).orElseThrow(() -> new NotFoundAnswerException(
                        ErrorMessage.NOT_FOUND_ANSWER_EXCEPTION,
                        String.format("요청한 %d로 Answer를 생성하지 못했습니다", answerId)
                )
        );

        return AnswerPageResponse.of(answer, programmerId);
    }

    @Transactional(readOnly = true)
    public Page<AnswerListResponse> readAllToListResponse(LanguageType languageType, Long backjoonId, Pageable pageable) {
        return readRepository.readAll(languageType, backjoonId, pageable);
    }

    @Transactional(readOnly = true)
    public Page<AnswerListResponse> readAllByProgrammerId(LanguageType language, Long backjoonId, Long programmerId, Pageable pageable) {
        return readRepository.readAllByProgrammerId(language, backjoonId, programmerId, pageable);
    }

    @Transactional
    public void update(Long programmerId, Long answerId, AnswerUpdateRequest request) {
        long changedRowNumber = writeRepository.update(programmerId, answerId, request);
        if (changedRowNumber == 0) {
            throw new NotFoundAnswerException(
                    ErrorMessage.NOT_FOUND_ANSWER_EXCEPTION,
                    String.format("요청한 %d로 Answer를 조회하지 못했습니다", answerId)
            );
        }
    }

    @Transactional
    public void delete(Long programmerId, Long answerId) {
        var answer = readRepository.read(answerId)
                .orElseThrow(() -> new NotFoundAnswerException(
                        ErrorMessage.NOT_FOUND_ANSWER_EXCEPTION,
                        String.format("요청한 Id %d를 가진 Answer가 존재하지 않습니다.", answerId))
                );

        if (answer.getProgrammer().getId().equals(programmerId)) {
            defaultAnswerRepository.delete(answer);
        } else throw new AnswerAndProgrammerDoNotMatchException(
                ErrorMessage.ANSWER_AND_PROGRAMMER_DO_NOT_MATCH,
                String.format("Programmer %d가 삭제 요청한 Answer %d는 요청자가 작성한 Answer가 아닙니다.", programmerId, answerId)
        );
    }
}