package com.codingMate.domain.programmer.vo;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
