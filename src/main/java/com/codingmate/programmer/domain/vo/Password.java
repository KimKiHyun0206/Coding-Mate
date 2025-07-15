package com.codingmate.programmer.domain.vo;

import com.codingmate.exception.dto.ErrorMessage;
import com.codingmate.exception.exception.programmer.InvalidPasswordException;
import lombok.Getter;

import java.util.regex.Pattern;

/**
 * Programmer 엔티티에서 password가 유효한 형식인지 검사하고 저장하기 위한 VO
 *
 * <li>최소 8글자, 글자 1개, 숫자 1개, 특수문자 1개</li>
 *
 * @author duskafka
 * @see com.codingmate.programmer.domain.Programmer
 * */
@Getter
public class Password {
    //(최소 8글자, 글자 1개, 숫자 1개, 특수문자 1개)
    private static final String PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$";
    private String password;

    public Password(String password) {
        validatePassword(password);
    }

    private void validatePassword(String password) {
        if (!Pattern.matches(PASSWORD_REGEX, password)) {
            throw new InvalidPasswordException(ErrorMessage.INVALID_PASSWORD_REGEX, "요청한 비밀번호 " + password + "가 알맞지 않습니다");
        }
        this.password = password;
    }

    public void setEncodePassword(String encodePassword) {
        this.password = encodePassword;
    }
}