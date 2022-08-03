package com.woowacourse.moragora.controller;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class UserControllerAspect {

    @Before("execution(* com.woowacourse.moragora.controller.UserController.*(..))")
    public void log(JoinPoint joinPoint) {
        final Object[] args = joinPoint.getArgs();
        for (final Object arg : args) {
            log.info("arg = {}", arg);
        }
        log.info("[error log] signature = {}", joinPoint.getSignature());
    }
}
