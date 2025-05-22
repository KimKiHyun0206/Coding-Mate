package com.codingMate.exception.exception.answer;

import com.codingMate.exception.BusinessException;
import com.codingMate.exception.dto.ErrorMessage;

public class AnswerNotCreateException extends BusinessException {
    public AnswerNotCreateException(ErrorMessage message) {
        super(message);
    }

    public AnswerNotCreateException(ErrorMessage message, String reason) {
        super(message, reason);
    }

    public AnswerNotCreateException(String reason) {
        super(reason);
    }
}
