package com.codingmate.exception.exception.answer;

import com.codingmate.exception.BusinessException;
import com.codingmate.exception.dto.ErrorMessage;

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