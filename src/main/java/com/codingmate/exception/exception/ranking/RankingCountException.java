package com.codingmate.exception.exception.ranking;

import com.codingmate.exception.BusinessException;
import com.codingmate.exception.dto.ErrorMessage;

public class RankingCountException extends BusinessException {
    public RankingCountException(ErrorMessage message) {
        super(message);
    }

    public RankingCountException(ErrorMessage message, String reason) {
        super(message, reason);
    }

    public RankingCountException(String reason) {
        super(reason);
    }
}