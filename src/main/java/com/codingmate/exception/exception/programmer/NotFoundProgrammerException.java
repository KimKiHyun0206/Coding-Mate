package com.codingmate.exception.exception.programmer;

import com.codingmate.exception.BusinessException;
import com.codingmate.exception.dto.ErrorMessage;

/**
 * 요청한 id가 유요한 {@code programmer_id}가 아니라 조회할 수 없을 때 발생하는 예외.
 * */
public class NotFoundProgrammerException extends BusinessException {
    public NotFoundProgrammerException(ErrorMessage message) {
        super(message);
    }

    public NotFoundProgrammerException(ErrorMessage message, String reason) {
        super(message, reason);
    }

    public NotFoundProgrammerException(String reason) {
        super(reason);
    }
}