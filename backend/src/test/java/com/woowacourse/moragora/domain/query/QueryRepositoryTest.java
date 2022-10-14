package com.woowacourse.moragora.domain.query;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.moragora.domain.attendance.Status;
import com.woowacourse.moragora.domain.event.Event;
import com.woowacourse.moragora.domain.meeting.Meeting;
import com.woowacourse.moragora.domain.participant.Participant;
import com.woowacourse.moragora.domain.participant.ParticipantAndCount;
import com.woowacourse.moragora.domain.user.User;
import com.woowacourse.moragora.support.DataSupport;
import com.woowacourse.moragora.support.fixture.EventFixtures;
import com.woowacourse.moragora.support.fixture.MeetingFixtures;
import com.woowacourse.moragora.support.fixture.UserFixtures;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@Import(DataSupport.class)
@DataJpaTest(showSql = false)
class QueryRepositoryTest {

    @Autowired
    private QueryRepository queryRepository;

    @Autowired
    private DataSupport dataSupport;


    @DisplayName("참가자 정보와 참가자의 활성화된 총 지각 횟수를 반환한다.")
    @Test
    void countParticipantsTardy() {
        // given
        final Meeting meeting = dataSupport.saveMeeting(MeetingFixtures.MORAGORA.create());
        final User user1 = dataSupport.saveUser(UserFixtures.SUN.create());
        final User user2 = dataSupport.saveUser(UserFixtures.PHILLZ.create());
        final User user3 = dataSupport.saveUser(UserFixtures.AZPI.create());
        final Participant participant1 = dataSupport.saveParticipant(user1, meeting);
        final Participant participant2 = dataSupport.saveParticipant(user2, meeting);
        final Participant participant3 = dataSupport.saveParticipant(user3, meeting);
        final Event event = dataSupport.saveEvent(EventFixtures.EVENT1.create(meeting));
        dataSupport.saveAttendance(participant1, event, Status.TARDY);
        dataSupport.saveAttendance(participant2, event, Status.TARDY);
        dataSupport.saveAttendance(participant3, event, Status.NONE);

        // when
        final List<ParticipantAndCount> participantAndCounts = queryRepository
                .countParticipantsTardyDto(List.of(participant1, participant2, participant3));

        // then
        final long expected = participantAndCounts.stream()
                .mapToLong(ParticipantAndCount::getTardyCount)
                .sum();
        final List<Participant> participants = participantAndCounts.stream()
                .map(ParticipantAndCount::getParticipant)
                .collect(Collectors.toList());

        assertAll(
                () -> assertThat(participantAndCounts).hasSize(3),
                () -> assertThat(expected).isEqualTo(2L),
                () -> assertThat(participants).containsAll(List.of(participant1, participant2, participant3))
        );
    }
}
