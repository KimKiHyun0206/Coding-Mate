package com.codingmate.answer.service;

import com.codingmate.answer.domain.Answer;
import com.codingmate.answer.repository.DefaultAnswerRepository;
import com.codingmate.common.annotation.Explanation;
import com.codingmate.exception.dto.ErrorMessage;
import com.codingmate.exception.exception.answer.NotFoundAnswerException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Explanation(
        responsibility = "단순히 Answer를 Query",
        detail = "엔티티 자체를 리턴하며, JPARepository를 사용한다",
        domain = "Answer",
        lastReviewed = "2025.06.05"
)
public class AnswerFinder {
    private final DefaultAnswerRepository answerRepository;

    @Transactional(readOnly = true)
    public Answer read(Long id) {
        return answerRepository.findById(id)
                .orElseThrow(() -> new NotFoundAnswerException(
                        ErrorMessage.NOT_FOUND_ANSWER,
                        "답변을 찾을 수 없습니다.")
                );
    }
}
