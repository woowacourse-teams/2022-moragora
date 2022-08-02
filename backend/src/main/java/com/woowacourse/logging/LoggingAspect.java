package com.woowacourse.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
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
    public void log(JoinPoint joinPoint) {
        log.info("[request log] signature = {}, requestBody = {}", joinPoint.getSignature(), joinPoint.getArgs());
    }
}
