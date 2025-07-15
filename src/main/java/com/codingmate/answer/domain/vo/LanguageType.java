package com.codingmate.answer.domain.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 프로그래밍 언어를 구분하는 열거형
 *
 * <p>백준 문제 풀이에서 사용 가능한 프로그래밍 언어를 정의한다. 각 언어는 고유한 식별자와 표시명을 가진다.</p>
 *
 * @author duskafka
 */
@Getter
@RequiredArgsConstructor
public enum LanguageType {
    JAVA("Java"),
    C("C"),
    CPP("C++"),
    PYTHON("Python"),
    JAVASCRIPT("JAVASCRIPT"),
    ;

    private final String language;
}