package com.codingmate.aop.timelog;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * Spring AOP를 사용해서 로깅 기능을 구현한 클래스
 *
 * <li>애노테이션 기반 AOP</li>
 * <li>메소드 시작과 끝에 로깅</li>
 *
 * @author duskafka
 * */
@Aspect
@Component
@Slf4j
public class ExecutionTimeLogger {

    @Around("@annotation(com.codingmate.aop.timelog.LogExecutionTime)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        Object result = null;
        String methodName = joinPoint.getSignature().toShortString();
        try {
            log.debug("[SYSTEM] {}({}) start", methodName, joinPoint.getArgs());
            result = joinPoint.proceed();  // 실제 메서드 실행
            return result;
        } finally {
            long time = System.currentTimeMillis() - start;
            log.debug("[SYSTEM] {} executed in {} ms", methodName, time);
        }
    }
}