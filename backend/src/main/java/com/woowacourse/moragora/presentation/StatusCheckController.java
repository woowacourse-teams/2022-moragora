package com.woowacourse.moragora.presentation;

import com.woowacourse.moragora.application.AttendanceScheduler;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatusCheckController {

    private final ApplicationContext applicationContext;

    public StatusCheckController(final ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * scheduler인지를 검증하는 API 입니다.
     */
    @GetMapping("/check/is-scheduler")
    public String isScheduler() {
        final AttendanceScheduler bean = getBean(AttendanceScheduler.class);
        if (bean != null) {
            return "true";
        }
        return "false";
    }

    private <T> T getBean(final Class<T> beanType) {
        try {
            return applicationContext.getBean(beanType);
        } catch (BeansException e) {
            return null;
        }
    }
}
