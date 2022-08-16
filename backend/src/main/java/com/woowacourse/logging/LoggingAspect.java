package com.woowacourse.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class LoggingAspect {

    @Before("(within(@org.springframework.stereotype.Controller *)"
            + "|| within(@org.springframework.web.bind.annotation.RestController *))"
            + "&& execution(public * *(..))")
    public void logRequest(JoinPoint joinPoint) {
        log.info("[request log] signature = {}, requestBody = {}", joinPoint.getSignature(), joinPoint.getArgs());
    }

    @AfterReturning(value = "(within(@org.springframework.stereotype.Controller *)"
            + "|| within(@org.springframework.web.bind.annotation.RestController *))"
            + "&& execution(public * *(..))",
            returning = "result")
    public void logResponse(JoinPoint joinPoint, Object result) {
        log.info("[response log] signature = {}, responseBody = {}", joinPoint.getSignature(), result);
    }

    @AfterThrowing(value = "(within(@org.springframework.stereotype.Controller *)"
            + "|| within(@org.springframework.web.bind.annotation.RestController *))"
            + "&& execution(public * *(..))",
            throwing = "ex")
    public void logDaoError(JoinPoint joinPoint, Exception ex) {
        log.error("[error log] signature = {}", joinPoint.getSignature(), ex);
    }
}

