package com.codingMate.util;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DateUtil {

    public static LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now();
    }
}