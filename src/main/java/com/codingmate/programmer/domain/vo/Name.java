package com.codingmate.programmer.domain.vo;

import com.codingmate.common.annotation.Explanation;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Explanation(
        responsibility = "이름 체크",
        detail = "이름이 한글로 저장되도록 체크한다",
        domain = "Programmer",
        lastReviewed = "2025.06.05"
)
public class Name {
    private static final String NAME_REGEX = "^[ㄱ-ㅎ가-힣]{1,5}$";
    private String name;

    public Name(String name) {
        validateEmail(name);
    }

    private void validateEmail(String name) {
        if(!Pattern.matches(NAME_REGEX, name)){
            //TODO 예외 발생시키기
        }
        this.name = name;
    }
}