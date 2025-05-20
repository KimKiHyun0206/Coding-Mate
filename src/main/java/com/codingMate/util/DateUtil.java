package com.codingMate.util;

import lombok.experimental.UtilityClass;

import java.util.Date;

@UtilityClass
public class DateUtil {
    public static Date getDate() {
        return new Date();
    }

    public static Date getTokenValidTime(Date date, Long validTime) {
        return new Date(date.getTime() + validTime);
    }
}