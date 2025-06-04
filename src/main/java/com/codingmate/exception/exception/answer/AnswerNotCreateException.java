package com.codingmate.exception.exception.answer;

import com.codingmate.exception.BusinessException;
import com.codingmate.exception.dto.ErrorMessage;

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
