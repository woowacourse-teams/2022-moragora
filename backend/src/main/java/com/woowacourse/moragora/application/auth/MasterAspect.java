package com.woowacourse.moragora.application.auth;

import com.woowacourse.moragora.exception.ClientRuntimeException;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class MasterAspect {

    private final AuthService authService;

    public MasterAspect(final AuthService authService) {
        this.authService = authService;
    }

    @Before("@annotation(com.woowacourse.moragora.application.auth.MasterAuthorization) && args(meetingId, .., loginId)")
    public void authorizeMaster(final Long meetingId, final Long loginId) {
        if (!authService.isMaster(meetingId, loginId)) {
            throw new ClientRuntimeException("마스터 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }
    }
}
