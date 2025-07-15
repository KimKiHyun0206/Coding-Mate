package com.codingmate.answer.service;

import com.codingmate.answer.domain.Answer;
import com.codingmate.answer.repository.DefaultAnswerRepository;
import com.codingmate.exception.dto.ErrorMessage;
import com.codingmate.exception.exception.answer.NotFoundAnswerException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 단순히 Answer를 찾기 위한 Finder 서비스
 *
 * <li>Answer 내부에서 Programmer 등 다른 연관 엔티티를 사용하여 지연 로딩을 발생시키지 않아도 되는 서비스에서 사용</li>
 * <li>만약 Answer이 포함하는 다른 엔티티 까지 사용해야 하는 비즈니스 로직이라면 이 클래스를 사용하면 안 된다</li>
 *
 * @author duskafka
 * */
@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
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
