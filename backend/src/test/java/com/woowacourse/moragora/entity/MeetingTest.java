package com.woowacourse.moragora.entity;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.woowacourse.moragora.exception.InvalidFormatException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class MeetingTest {

    @DisplayName("미팅을 생성한다.")
    @Test
    void createMeeting() {
        // given, when, then
        assertThatCode(() -> new Meeting("모임")).doesNotThrowAnyException();
    }

    @DisplayName("미팅을 생성할 때, 이름이 50자를 초과하면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"012345678901234567890123456789012345678901234567891",
            "영일이삼사오육칠팔구영일이삼사오육칠팔구영일이삼사오육칠팔구영일이삼사오육칠팔구영일이삼사오육칠팔구영",
            "abcdefghijabcdefghijabcdefghijabcdefghijabcdefghija"})
    void createMeeting_throwsException_ifNameOver50(final String name) {
        // given, when, then
        assertThatThrownBy(() -> new Meeting(name))
                .isInstanceOf(InvalidFormatException.class);
    }

    @DisplayName("미팅의 이름을 수정한다.")
    @Test
    void updateName() {
        // given
        final Meeting meeting = new Meeting("모임");

        // when, then
        assertThatCode(() -> meeting.updateName("새로운 이름"))
                .doesNotThrowAnyException();
    }

    @DisplayName("50자를 초과하는 이름으로 수정하면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"012345678901234567890123456789012345678901234567891",
            "영일이삼사오육칠팔구영일이삼사오육칠팔구영일이삼사오육칠팔구영일이삼사오육칠팔구영일이삼사오육칠팔구영",
            "abcdefghijabcdefghijabcdefghijabcdefghijabcdefghija"})
    void updateName_throwsException_ifNameOver50(final String name) {
        // given
        final Meeting meeting = new Meeting("모임");

        // when, then
        assertThatThrownBy(() -> meeting.updateName(name))
                .isInstanceOf(InvalidFormatException.class);
    }
}
