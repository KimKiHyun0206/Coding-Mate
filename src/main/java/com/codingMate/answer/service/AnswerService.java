package com.codingMate.answer.service;

import com.codingMate.answer.domain.Answer;
import com.codingMate.answer.domain.vo.LanguageType;
import com.codingMate.answer.repository.AnswerReadRepository;
import com.codingMate.answer.repository.AnswerWriteRepository;
import com.codingMate.programmer.domain.Programmer;
import com.codingMate.answer.dto.request.AnswerCreateRequest;
import com.codingMate.answer.dto.request.AnswerUpdateRequest;
import com.codingMate.answer.dto.response.AnswerCreateResponse;
import com.codingMate.answer.dto.response.AnswerListResponse;
import com.codingMate.answer.dto.response.AnswerPageResponse;
import com.codingMate.exception.exception.answer.AnswerAndProgrammerDoNotMatchException;
import com.codingMate.exception.exception.answer.AnswerNotCreateException;
import com.codingMate.exception.exception.answer.NotFoundAnswerException;
import com.codingMate.exception.exception.programmer.NotFoundProgrammerException;
import com.codingMate.answer.repository.DefaultAnswerRepository;
import com.codingMate.programmer.repository.DefaultProgrammerRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        Programmer writer = defaultProgrammerRepository.findById(programmerId).orElseThrow(() -> new NotFoundProgrammerException("Answer를 생성하던 중 Programmer를 조회하지 못했습니다. " + programmerId));

        Answer createdResult = defaultAnswerRepository.save(request.toEntity());
        createdResult.setProgrammer(writer);

        return new AnswerCreateResponse(createdResult.getId());
    }

    @Transactional(readOnly = true)
    public AnswerPageResponse read(Long answerId, Long programmerId) {
        var answer = readRepository.read(answerId);
        if (answer == null) throw new AnswerNotCreateException("Answer를 조회하지 못했습니다. " + answerId);

        var response = AnswerPageResponse.from(answer);
        response.setIsRequesterIsOwner(programmerId);

        return response;
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
    public AnswerPageResponse update(Long programmerId, Long answerId, AnswerUpdateRequest request) {
        long changedRowNumber = writeRepository.update(programmerId, answerId, request);
        if (changedRowNumber != 1) throw new NotFoundAnswerException(answerId);
        return read(answerId, programmerId);
    }

    @Transactional
    public void delete(Long programmerId, Long answerId) {
        Answer answer = readRepository.read(answerId);
        if (answer == null) throw new NotFoundAnswerException("삭제하기 위한 Answer를 조회할 수 없습니다. " + answerId);
        if (answer.getProgrammer().getId().equals(programmerId)) {
            defaultAnswerRepository.delete(answer);
        } else throw new AnswerAndProgrammerDoNotMatchException("요청한 Programmer가 작성한 Answer가 아닙니다. " + programmerId);
    }
}