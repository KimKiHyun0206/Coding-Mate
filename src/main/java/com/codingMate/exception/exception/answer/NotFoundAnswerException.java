package com.codingMate.exception.exception.answer;

import com.codingMate.exception.BusinessException;
import com.codingMate.exception.dto.ErrorMessage;

public class NotFoundAnswerException extends BusinessException {
    public NotFoundAnswerException(Long answerId) {
        super(ErrorMessage.NOT_FOUND_ANSWER_EXCEPTION, "answerId : " + answerId);
    }
}