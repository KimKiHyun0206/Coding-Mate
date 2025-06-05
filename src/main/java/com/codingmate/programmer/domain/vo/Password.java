package com.codingmate.programmer.domain.vo;

import com.codingmate.common.annotation.Explanation;
import com.codingmate.exception.dto.ErrorMessage;
import com.codingmate.exception.exception.programmer.InvalidPasswordException;
import lombok.Getter;

import java.util.regex.Pattern;

@Getter
@Explanation(
        responsibility = "비밀번호",
        detail = "비밀번호가 정규식에 맞는지 확인하고 맞다면 비밀번호를 인코딩한다",
        domain = "Programmer",
        lastReviewed = "2025.06.05"
)
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