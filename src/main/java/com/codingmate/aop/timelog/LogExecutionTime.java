package com.codingmate.aop.timelog;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Spring AOP 기반으로 애노테이션 기반 로거를 만들기 위한 애노테이션
 *
 * @author duskafka
 * */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogExecutionTime {
}