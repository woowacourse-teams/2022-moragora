package com.woowacourse.moragora.entity;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.woowacourse.moragora.exception.meeting.IllegalStartEndDateException;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MeetingTest {

    @DisplayName("미팅을 생성한다.")
    @Test
    void createMeeting() {
        // given, when, then
        assertThatCode(() -> new Meeting(
                "모임",
                LocalDate.of(2022, 6, 11),
                LocalDate.of(2022, 6, 14))
        ).doesNotThrowAnyException();
    }

    @DisplayName("시작 날짜가 종료 날짜보다 늦은 경우 예외가 발생한다.")
    @Test
    void createMeeting_throwsException_ifStartDateIsLaterThanEndDate() {
        // given, when, then
        assertThatThrownBy(() -> new Meeting(
                "모임",
                LocalDate.of(2022, 6, 15),
                LocalDate.of(2022, 6, 14)
        )).isInstanceOf(IllegalStartEndDateException.class);
    }
}
