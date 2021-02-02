package com.firesale.api.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class DurationAspect {

    @Around("@annotation(com.firesale.api.aspect.LogDuration)")
    public Object measureDuration(ProceedingJoinPoint joinPoint) throws Throwable {
        final long start = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();
        final long executionTime = System.currentTimeMillis() - start;
        log.trace(joinPoint.getSignature() + " executed in " + executionTime + "ms");
        return proceed;
    }
}
