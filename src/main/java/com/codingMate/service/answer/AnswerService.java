package com.codingMate.service.answer;

import com.codingMate.domain.answer.Answer;
import com.codingMate.domain.answer.vo.LanguageType;
import com.codingMate.domain.programmer.Programmer;
import com.codingMate.dto.request.answer.AnswerCreateRequest;
import com.codingMate.dto.request.answer.AnswerUpdateRequest;
import com.codingMate.dto.response.answer.AnswerCreateResponse;
import com.codingMate.dto.response.answer.AnswerListResponse;
import com.codingMate.dto.response.answer.AnswerPageResponse;
import com.codingMate.dto.response.answer.AnswerResponse;
import com.codingMate.exception.exception.answer.AnswerAndProgrammerDoNotMatchException;
import com.codingMate.exception.exception.answer.AnswerNotCreateException;
import com.codingMate.exception.exception.answer.NotFoundAnswerException;
import com.codingMate.exception.exception.programmer.NotFoundProgrammerException;
import com.codingMate.repository.answer.CustomAnswerRepository;
import com.codingMate.repository.answer.DefaultAnswerRepository;
import com.codingMate.repository.programmer.CustomProgrammerRepository;
import com.codingMate.repository.programmer.DefaultProgrammerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnswerService {
    private final CustomAnswerRepository answerRepository;
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
        var answer = answerRepository.read(answerId);
        if (answer == null) throw new AnswerNotCreateException("Answer를 조회하지 못했습니다. " + answerId);

        var response = AnswerPageResponse.from(answer);
        response.setIsRequesterIsOwner(programmerId);

        return response;
    }

    @Transactional(readOnly = true)
    public List<AnswerListResponse> readAllToListResponse(LanguageType languageType, Long backjoonId) {
        return answerRepository.readAll(languageType, backjoonId);
    }

    @Transactional(readOnly = true)
    public List<AnswerListResponse> readAllByProgrammerId(LanguageType language, Long backjoonId, Long programmerId) {
        return answerRepository.readAllByProgrammerId(language, backjoonId, programmerId);
    }

    @Transactional
    public AnswerPageResponse update(Long programmerId, Long answerId, AnswerUpdateRequest request) {
        long changedRowNumber = answerRepository.update(programmerId, answerId, request);
        if (changedRowNumber != 1) throw new NotFoundAnswerException(answerId);
        return read(answerId, programmerId);
    }

    @Transactional
    public boolean delete(Long programmerId, Long answerId) {
        Answer answer = answerRepository.read(answerId);
        if (answer == null) throw new NotFoundAnswerException("삭제하기 위한 Answer를 조회할 수 없습니다. " + answerId);
        if (answer.getProgrammer().getId().equals(programmerId)) {
            defaultAnswerRepository.delete(answer);
        } else throw new AnswerAndProgrammerDoNotMatchException("요청한 Programmer가 작성한 Answer가 아닙니다. " + programmerId);
        return true;
    }
}