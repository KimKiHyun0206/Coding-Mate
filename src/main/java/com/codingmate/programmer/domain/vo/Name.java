package com.codingmate.programmer.domain.vo;

import com.codingmate.config.constant.RegexConstants;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

/**
 * Programmer 엔티티에서 name이 유효한지 검증하는 VO
 *
 * <li>한글이어야 하마 최소 1자 최대 5자까지 허용한다</li>
 *
 * @author duskafka
 * @see com.codingmate.programmer.domain.Programmer
 * */
@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Name {
    private static final String NAME_REGEX = RegexConstants.KOREAN_NAME_REGEX;
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