package com.woowacourse.moragora.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.woowacourse.moragora.exception.meeting.IllegalStartEndDateException;
import java.time.LocalDate;
import java.time.LocalTime;
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
                LocalDate.of(2022, 6, 14),
                LocalTime.of(12, 0),
                LocalTime.of(15, 0))
        ).doesNotThrowAnyException();
    }

    @DisplayName("시작 날짜가 종료 날짜보다 늦은 경우 예외가 발생한다.")
    @Test
    void createMeeting_throwsException_ifStartDateIsLaterThanEndDate() {
        // given, when, then
        assertThatThrownBy(() -> new Meeting(
                "모임",
                LocalDate.of(2022, 6, 15),
                LocalDate.of(2022, 6, 14),
                LocalTime.of(12, 0),
                LocalTime.of(15, 0)
        )).isInstanceOf(IllegalStartEndDateException.class);
    }

    @DisplayName("총 출석일 수를 증가시킨다.")
    @Test
    void increaseMeetingCount() {
        // given
        final Meeting meeting = new Meeting(
                "모임",
                LocalDate.of(2022, 6, 11),
                LocalDate.of(2022, 6, 14),
                LocalTime.of(12, 0),
                LocalTime.of(15, 0)
        );

        // when
        meeting.increaseMeetingCount();

        // then
        assertThat(meeting.getAttendanceCount()).isOne();
    }
}
