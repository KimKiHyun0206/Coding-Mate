package com.codingmate.common.annotation;

import java.lang.annotation.*;

@Target(ElementType.TYPE) // 이 애노테이션이 클래스, 인터페이스, enum에만 적용될 수 있도록 합니다.
@Retention(RetentionPolicy.SOURCE)
public @interface Explanation {
    //클래스의 목적을 설명합니다.
    String responsibility() default ""; // 기본값을 빈 문자열로 설정하여 선택적으로 사용할 수 있도록 합니다.

    //클래스의 주요 기능이나 역할을 설명합니다.
    String detail() default "";

    //클래스가 담당하는 도메인을 설명합니다.
    String domain() default "";

    //이 클래스가 마지막으로 수정된 날짜를 기록합니다
    String lastReviewed() default "YYYY.MM.DD";
}