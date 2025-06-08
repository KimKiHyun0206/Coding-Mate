package com.codingmate.exception.exception.jwt;

import com.codingmate.exception.BusinessException;
import com.codingmate.exception.dto.ErrorMessage;

public class JitNotMatch extends BusinessException {
    public JitNotMatch(ErrorMessage message) {
        super(message);
    }

    public JitNotMatch(ErrorMessage message, String reason) {
        super(message, reason);
    }

    public JitNotMatch(String reason) {
        super(reason);
    }
}
