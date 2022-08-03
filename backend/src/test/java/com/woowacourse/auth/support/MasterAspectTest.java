package com.woowacourse.auth.support;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.woowacourse.moragora.exception.ClientRuntimeException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MasterAspectTest {

    @Autowired
    private MasterAspect masterAspect;

    @DisplayName("로그인한 유저가 해당 미팅의 Master인지 판단한다.")
    @Test
    void authorizeMaster() {
        // given
        final Long meetingId = 1L;
        final Long loginId = 1L;

        // when, then
        assertDoesNotThrow(() -> masterAspect.authorizeMaster(meetingId, loginId));
    }

    @DisplayName("로그인한 유저가 해당 미팅의 Master인지 판단한다(아닌 경우)")
    @Test
    void authorizeMaster_NotMaster() {
        // given
        final Long meetingId = 1L;
        final Long loginId = 2L;

        // when, then
        assertThatThrownBy(() -> masterAspect.authorizeMaster(meetingId, loginId))
                .isInstanceOf(ClientRuntimeException.class)
                .hasMessage("마스터 권한이 없습니다.");
    }
}
