package com.codingMate.exception.exception.answer;

import com.codingMate.exception.BusinessException;
import com.codingMate.exception.dto.ErrorMessage;

public class AnswerAndProgrammerDoNotMatchException extends BusinessException {
    public AnswerAndProgrammerDoNotMatchException(ErrorMessage message) {
        super(message);
    }

    public AnswerAndProgrammerDoNotMatchException(ErrorMessage message, String reason) {
        super(message, reason);
    }

    public AnswerAndProgrammerDoNotMatchException(String reason) {
        super(reason);
    }
}