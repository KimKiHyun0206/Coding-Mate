package com.codingmate.answer.domain.vo;

import com.codingmate.common.annotation.Explanation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Explanation(
        responsibility = "Answer에 저장할 언어 타입을 지정해줍니다",
        domain = "Answer",
        lastReviewed = "2025.06.05"
)
public enum LanguageType {
    JAVA("Java"),
    C("C"),
    CPP("C++"),
    PYTHON("Python"),
    JAVASCRIPT("JAVASCRIPT"),
    ;

    private final String language;
}