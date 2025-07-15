package com.codingmate.util;

import lombok.experimental.UtilityClass;
import java.util.Date;

/**
 * 현재 시간을 가져오기 위한 유틸 클래스
 *
 * @author duskafka
 * */
@UtilityClass
public class DateUtil {
    public static Date getDate() {
        return new Date();
    }

    public static Date getTokenValidTime(Date date, Long validTime) {
        return new Date(date.getTime() + validTime);
    }
}