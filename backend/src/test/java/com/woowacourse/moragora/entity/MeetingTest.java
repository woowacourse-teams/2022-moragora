package com.woowacourse.moragora.entity;

import static org.assertj.core.api.Assertions.assertThatCode;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MeetingTest {

    @DisplayName("미팅을 생성한다.")
    @Test
    void createMeeting() {
        // given, when, then
        assertThatCode(() -> new Meeting("모임")).doesNotThrowAnyException();
    }
}
