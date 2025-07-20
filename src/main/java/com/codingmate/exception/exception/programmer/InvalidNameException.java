package com.codingmate.exception.exception.programmer;

import com.codingmate.exception.BusinessException;
import com.codingmate.exception.dto.ErrorMessage;

/**
 * 이름이 정규식에 알맞지 않을 때 발생하는 예외
 * */
public class InvalidNameException extends BusinessException {
    public InvalidNameException(ErrorMessage message) {
        super(message);
    }

    public InvalidNameException(ErrorMessage message, String reason) {
        super(message, reason);
    }

    public InvalidNameException(String reason) {
        super(reason);
    }
}