package com.codingmate.exception.exception.programmer;

import com.codingmate.exception.BusinessException;
import com.codingmate.exception.dto.ErrorMessage;

/**
 * 사용자가 회원가입할 때 요청한 {@code login_id}가 이미 데이터베이스에 존재할 때 발생하는 예외.
 * */
public class DuplicateProgrammerLoginIdException extends BusinessException {
    public DuplicateProgrammerLoginIdException(ErrorMessage message) {
        super(message);
    }

    public DuplicateProgrammerLoginIdException(ErrorMessage message, String reason) {
        super(message, reason);
    }

    public DuplicateProgrammerLoginIdException(String reason) {
        super(reason);
    }
}