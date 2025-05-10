package com.codingMate.exception.exception.answer;

import com.codingMate.exception.BusinessException;

public class AnswerNotCreateException extends BusinessException {
    public AnswerNotCreateException(String reason) {
        super(reason);
    }
}
