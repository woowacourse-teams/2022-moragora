package com.woowacourse.moragora.domain.attendance;

import static com.woowacourse.moragora.support.fixture.EventFixtures.EVENT1;
import static com.woowacourse.moragora.support.fixture.EventFixtures.EVENT2;
import static com.woowacourse.moragora.support.fixture.EventFixtures.EVENT3;
import static com.woowacourse.moragora.support.fixture.MeetingFixtures.MORAGORA;
import static com.woowacourse.moragora.support.fixture.UserFixtures.AZPI;
import static com.woowacourse.moragora.support.fixture.UserFixtures.PHILLZ;
import static com.woowacourse.moragora.support.fixture.UserFixtures.SUN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.moragora.domain.event.Event;
import com.woowacourse.moragora.domain.meeting.Meeting;
import com.woowacourse.moragora.domain.participant.Participant;
import com.woowacourse.moragora.domain.user.User;
import com.woowacourse.moragora.support.DataSupport;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

@Import(DataSupport.class)
@DataJpaTest(showSql = false)
@Transactional
public class CoffeeStatRepositoryTest {

    @Autowired
    private CoffeeStatRepository coffeeStatRepository;

    @Autowired
    private DataSupport dataSupport;

    @DisplayName("참가자 정보와 참가자의 활성화된 총 지각 횟수를 반환한다.")
    @Test
    void countCoffeeStats() {
        // given
        final Meeting meeting = dataSupport.saveMeeting(MORAGORA.create());

        final User user1 = dataSupport.saveUser(SUN.create());
        final User user2 = dataSupport.saveUser(PHILLZ.create());
        final User user3 = dataSupport.saveUser(AZPI.create());

        final Participant participant1 = dataSupport.saveParticipant(user1, meeting);
        final Participant participant2 = dataSupport.saveParticipant(user2, meeting);
        final Participant participant3 = dataSupport.saveParticipant(user3, meeting);

        final Event event1 = dataSupport.saveEvent(EVENT1.create(meeting));
        final Event event2 = dataSupport.saveEvent(EVENT2.create(meeting));
        final Event event3 = dataSupport.saveEvent(EVENT3.create(meeting));

        final Attendance attendance1 = dataSupport.saveAttendance(participant1, event1, Status.TARDY);
        final Attendance attendance2 = dataSupport.saveAttendance(participant2, event1, Status.TARDY);
        dataSupport.saveAttendance(participant3, event1, Status.NONE);
        dataSupport.saveAttendance(participant1, event2, Status.NONE);
        final Attendance attendance3 = dataSupport.saveAttendance(participant2, event2, Status.TARDY);
        dataSupport.saveAttendance(participant3, event2, Status.NONE);
        dataSupport.saveAttendance(participant1, event3, Status.NONE);
        dataSupport.saveAttendance(participant2, event3, Status.TARDY);
        dataSupport.saveAttendance(participant3, event3, Status.NONE);

        final List<Participant> participants = List.of(participant1, participant2, participant3);
        final PageRequest pageRequest = PageRequest.of(0, participants.size());

        // when
        final List<Attendance> attendances = coffeeStatRepository.findCoffeeStatsLimitParticipant(
                pageRequest, participants);

        final List<Long> ids = attendances.stream()
                .map(Attendance::getId)
                .collect(Collectors.toList());

        // then
        assertAll(
                () -> assertThat(attendances.size()).isEqualTo(3),
                () -> assertThat(attendances).contains(attendance1, attendance2, attendance3)
        );
    }
}
