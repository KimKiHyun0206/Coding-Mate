package com.codingMate.programmer.domain.vo;

import com.codingMate.exception.dto.ErrorMessage;
import com.codingMate.exception.exception.programmer.InvalidEmailException;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Email {
    private static final String EMAIL_REGEX = "[0-9a-zA-Z]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
    private String email;

    public Email(String email) {
        validateEmail(email);
    }

    private void validateEmail(String email) {
        if (!Pattern.matches(EMAIL_REGEX, email)) {
            throw new InvalidEmailException(ErrorMessage.INVALID_EMAIL_REGEX, "유효한 Email 형식이 아닙니다");
        }
        this.email = email;
    }
}