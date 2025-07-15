package com.codingmate.exception.exception.ranking;

import com.codingmate.exception.BusinessException;
import com.codingmate.exception.dto.ErrorMessage;

/**
 * Redis에서 조회해온 {@code List<SolveCountRankingDto>}가 타입이 맞지 않다면 발생하는 예외.
 * */
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