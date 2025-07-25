package com.codingmate.exception.exception.answer;

import com.codingmate.exception.BusinessException;
import com.codingmate.exception.dto.ErrorMessage;

/**
 * Answer 엔티티 수정에 실패했을 때 발생하는 예외.
 * */
public class AnswerUpdateFailException extends BusinessException {
    public AnswerUpdateFailException(ErrorMessage message) {
        super(message);
    }

    public AnswerUpdateFailException(ErrorMessage message, String reason) {
        super(message, reason);
    }

    public AnswerUpdateFailException(String reason) {
        super(reason);
    }
}