package com.codingmate.exception.exception.programmer;

import com.codingmate.exception.BusinessException;
import com.codingmate.exception.dto.ErrorMessage;

/**
 * 요청한 {@code login_id}가 데이터베이스의 {@code login_id}와 일치하지 않을 때 발생하는 오류.
 * */
public class LoginIdNotMatchException extends BusinessException {
    public LoginIdNotMatchException(ErrorMessage message) {
        super(message);
    }

    public LoginIdNotMatchException(ErrorMessage message, String reason) {
        super(message, reason);
    }

    public LoginIdNotMatchException(String reason) {
        super(reason);
    }
}
