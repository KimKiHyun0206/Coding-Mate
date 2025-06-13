package com.codingmate.util;

import com.codingmate.common.annotation.Explanation;
import lombok.experimental.UtilityClass;

import java.util.Date;

@UtilityClass
@Explanation(
        responsibility = "현재 시간을 가져온다",
        lastReviewed = "2025.06.05"
)
public class DateUtil {
    public static Date getDate() {
        return new Date();
    }

    public static Date getTokenValidTime(Date date, Long validTime) {
        return new Date(date.getTime() + validTime);
    }
}