package com.codingmate.exception.exception.answer;

import com.codingmate.exception.BusinessException;
import com.codingmate.exception.dto.ErrorMessage;

public class NotFoundAnswerException extends BusinessException {
    public NotFoundAnswerException(ErrorMessage message) {
        super(message);
    }

    public NotFoundAnswerException(ErrorMessage message, String reason) {
        super(message, reason);
    }

    public NotFoundAnswerException(String reason) {
        super(reason);
    }
}