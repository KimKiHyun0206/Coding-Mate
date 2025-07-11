package com.codingmate.aop.timelog;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

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