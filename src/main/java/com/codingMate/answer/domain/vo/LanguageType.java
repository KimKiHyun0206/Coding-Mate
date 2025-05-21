package com.codingMate.answer.domain.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

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