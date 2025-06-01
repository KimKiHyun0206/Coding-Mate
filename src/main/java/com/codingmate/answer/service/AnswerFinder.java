package com.codingmate.answer.service;

import com.codingmate.answer.domain.Answer;
import com.codingmate.answer.repository.DefaultAnswerRepository;
import com.codingmate.exception.dto.ErrorMessage;
import com.codingmate.exception.exception.answer.NotFoundAnswerException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class AnswerFinder {
    private final DefaultAnswerRepository answerRepository;

    @Transactional(readOnly = true)
    public Answer read(Long id) {
        return answerRepository.findById(id)
                .orElseThrow(() -> new NotFoundAnswerException(
                        ErrorMessage.NOT_FOUND_ANSWER_EXCEPTION,
                        "답변을 찾을 수 없습니다.")
                );
    }
}
