package com.codingmate.exception.exception.ranking;

import com.codingmate.exception.BusinessException;
import com.codingmate.exception.dto.ErrorMessage;

public class NoRankingException extends BusinessException {
    public NoRankingException(ErrorMessage message) {
        super(message);
    }

    public NoRankingException(ErrorMessage message, String reason) {
        super(message, reason);
    }

    public NoRankingException(String reason) {
        super(reason);
    }
}