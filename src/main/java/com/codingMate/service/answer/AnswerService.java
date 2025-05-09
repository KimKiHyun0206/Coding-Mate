package com.codingMate.service.answer;

import com.codingMate.domain.answer.Answer;
import com.codingMate.domain.answer.vo.LanguageType;
import com.codingMate.dto.request.answer.AnswerCreateRequest;
import com.codingMate.dto.request.answer.AnswerUpdateRequest;
import com.codingMate.dto.response.answer.AnswerListResponse;
import com.codingMate.dto.response.answer.AnswerResponse;
import com.codingMate.exception.exception.answer.NotFoundAnswerException;
import com.codingMate.repository.answer.CustomAnswerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnswerService {
    private final CustomAnswerRepository answerRepository;

    @Transactional
    public AnswerResponse create(Long programmerId, AnswerCreateRequest request) {
        return answerRepository.create(programmerId, request).toDto();
    }

    @Transactional(readOnly = true)
    public AnswerResponse read(Long answerId) {
        return answerRepository.read(answerId).toDto();
    }

    @Transactional(readOnly = true)
    public List<AnswerListResponse> readAllToListResponse(LanguageType languageType, Long backjoonId){
        return answerRepository.readAll(languageType, backjoonId);
    }

    @Transactional(readOnly = true)
    public List<AnswerResponse> readAllByProgrammerId(Long programmerId) {
        return answerRepository.readAllByProgrammerId(programmerId).stream().map(Answer::toDto).toList();
    }

    @Transactional
    public AnswerResponse update(Long programmerId, Long answerId,  AnswerUpdateRequest request) {
        long changedRowNumber = answerRepository.update(programmerId, answerId, request);
        if(changedRowNumber == 0) throw new NotFoundAnswerException(answerId);
        return read(answerId);
    }

    @Transactional
    public void delete(Long programmerId, Long answerId) {
        answerRepository.delete(programmerId, answerId);
    }
}