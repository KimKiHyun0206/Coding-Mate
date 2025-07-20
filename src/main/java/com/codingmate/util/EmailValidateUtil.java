package com.codingmate.util;

import com.codingmate.config.constant.RegexConstants;
import com.codingmate.exception.dto.ErrorMessage;
import com.codingmate.exception.exception.email.IllegalEmailRegexException;
import lombok.experimental.UtilityClass;

import java.util.regex.Pattern;

@UtilityClass
public class EmailValidateUtil {
    // RFC 5322 공식 이메일 형식에 근접한 정규식
    private static final String EMAIL_REGEX = RegexConstants.EMAIL_REGEX;
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);


    /**
     * 이메일 형식 검증 메서드
     *
     * @param email 검증할 이메일 문자열
     */
    public static void isValid(String email) {
        if (email == null || email.isEmpty() || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalEmailRegexException(
                    ErrorMessage.ILLEGAL_EMAIL_REGEX,
                    String.format("요청한 이메일(%s)는 이메일 형식에 알맞지 않습니다.", email)
            );
        }
    }
}