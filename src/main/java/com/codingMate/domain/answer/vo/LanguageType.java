package com.codingMate.domain.answer.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LanguageType {
    JAVA("Java"),
    C("C"),
    CPP("C++"),
    PYTHON("Python"),
    ;

    private final String language;
}
