package com.codingMate.exception.exception.answer;

import com.codingMate.exception.BusinessException;
import com.codingMate.exception.dto.ErrorMessage;

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