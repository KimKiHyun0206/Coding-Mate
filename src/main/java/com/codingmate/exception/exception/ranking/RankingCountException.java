package com.codingmate.exception.exception.ranking;

import com.codingmate.exception.BusinessException;
import com.codingmate.exception.dto.ErrorMessage;

/**
 * 랭킹 카운트 처리 중 오류가 발생했을 때 발생하는 예외입니다.
 *
 * <p>다음 상황에서 발생합니다.</p>
 * <ul>
 *     <li>Redis에 랭킹 데이터를 저장하는 중 오류가 발생한 경우</li>
 *     <li>배치 작업에서 랭킹 카운트를 처리하는 중 예외가 발생한 경우</li>
 *     <li>랭킹 데이터의 개수나 형식이 올바르지 않은 경우</li>
 * </ul>
 * */
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