package com.codingmate.exception.exception.ranking;

import com.codingmate.exception.BusinessException;
import com.codingmate.exception.dto.ErrorMessage;

public class RankingIllegalTypeException extends BusinessException {
    public RankingIllegalTypeException(ErrorMessage message) {
        super(message);
    }

    public RankingIllegalTypeException(ErrorMessage message, String reason) {
        super(message, reason);
    }

    public RankingIllegalTypeException(String reason) {
        super(reason);
    }
}