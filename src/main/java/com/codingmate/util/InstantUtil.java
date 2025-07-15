package com.codingmate.util;

import lombok.experimental.UtilityClass;
import java.time.Instant;


/**
 * 리프레쉬 토큰에 사용되는 Score를 변환하는 유틸 클래스
 *
 * <li>Instant를 Score로 바꾼다</li>
 * <li>Score를 Instant로 바꾼다</li>
 *
 * @author duskafka
 * */
@UtilityClass
public class InstantUtil {

    /**
     * java.time.Instant 객체를 Redis Sorted Set의 score로 사용될 Epoch 밀리초 값으로 변환합니다.
     * @param instant 변환할 Instant 객체
     * @return Epoch 밀리초 값 (long 타입)
     */
    public static long instantToScore(Instant instant){
        return instant.toEpochMilli();
    }

    // 필요하다면, Epoch 밀리초 값을 Instant 객체로 다시 변환하는 메서드도 추가할 수 있습니다.
    /**
     * Epoch 밀리초 값을 java.time.Instant 객체로 변환합니다.
     * @param epochMilli Epoch 밀리초 값
     * @return 변환된 Instant 객체
     */
    public static Instant scoreToInstant(long epochMilli) {
        return Instant.ofEpochMilli(epochMilli);
    }
}
