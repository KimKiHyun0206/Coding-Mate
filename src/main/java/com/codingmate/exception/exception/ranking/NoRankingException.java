package com.codingmate.exception.exception.ranking;

import com.codingmate.exception.BusinessException;
import com.codingmate.exception.dto.ErrorMessage;

/**
 * 데이터베이스에서 사용자의 랭킹을 조회할 수 없을 때 발생하는 예외.
 * */
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