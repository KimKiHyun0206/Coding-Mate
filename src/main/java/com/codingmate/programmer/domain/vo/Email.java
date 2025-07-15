package com.codingmate.programmer.domain.vo;

import com.codingmate.exception.dto.ErrorMessage;
import com.codingmate.exception.exception.programmer.InvalidEmailException;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

/**
 * Programmer 엔티티에서 email이 유효한 형식인지 검사하고 저장하기 위한 VO
 *
 * <li>@를 포함해야 하고 .이 있어야 한다.</li>
 *
 * @author duskafka
 * @see com.codingmate.programmer.domain.Programmer
 * */
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