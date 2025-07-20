package com.codingmate.exception.exception.email;

import com.codingmate.exception.BusinessException;
import com.codingmate.exception.dto.ErrorMessage;

/**
 * 이메일 인증을 위해 사용자에게 보낼 이메일 메시지를 만들다가 예외가 발생했을 때 던져질 예외
 * */
public class EmailMessagingException extends BusinessException {
    public EmailMessagingException(ErrorMessage message) {
        super(message);
    }

    public EmailMessagingException(ErrorMessage message, String reason) {
        super(message, reason);
    }

    public EmailMessagingException(String reason) {
        super(reason);
    }
}