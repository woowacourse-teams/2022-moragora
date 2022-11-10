package com.woowacourse.moragora.domain.query;

import static com.woowacourse.moragora.support.fixture.EventFixtures.EVENT1;
import static com.woowacourse.moragora.support.fixture.MeetingFixtures.MORAGORA;
import static com.woowacourse.moragora.support.fixture.UserFixtures.AZPI;
import static com.woowacourse.moragora.support.fixture.UserFixtures.PHILLZ;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.moragora.domain.attendance.Status;
import com.woowacourse.moragora.domain.event.Event;
import com.woowacourse.moragora.domain.meeting.Meeting;
import com.woowacourse.moragora.domain.participant.Participant;
import com.woowacourse.moragora.domain.user.User;
import com.woowacourse.moragora.support.DataSupport;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CompositionRepositoryTest {

    @Autowired
    private CompositionRepository compositionRepository;

    @Autowired
    private DataSupport dataSupport;

    @Test
    void meetingWithTardyCount() {
        // given
        final Meeting meeting = MORAGORA.create();
        final User user1 = PHILLZ.create();
        final User user2 = AZPI.create();
        final Participant participant1 = dataSupport.saveParticipant(user1, meeting, true);
        final Participant participant2 = dataSupport.saveParticipant(user2, meeting, false);
        final Event event = EVENT1.create(meeting);

        final Meeting savedMeeting = dataSupport.saveMeeting(meeting);
        dataSupport.saveUsers(user1, user2);
        dataSupport.saveEvent(event);
        dataSupport.saveAttendance(participant1, event, Status.TARDY);
        dataSupport.saveAttendance(participant2, event, Status.PRESENT);

        // when
        final Meeting foundMeeting = compositionRepository.meetingWithTardyCount(savedMeeting.getId());

        // then
        final List<Participant> foundParticipants = foundMeeting.getParticipants();
        final Participant foundParticipant1 = foundParticipants.get(0);
        final Participant foundParticipant2 = foundParticipants.get(1);
        final List<User> foundUsers = List.of(foundParticipant1.getUser(), foundParticipant2.getUser());
        final List<Long> foundTardyCounts = List.of(foundParticipant1.getTardyCount(),
                foundParticipant2.getTardyCount());

        assertAll(
                () -> assertThat(foundMeeting)
                        .usingRecursiveComparison()
                        .ignoringFields("participants")
                        .isEqualTo(meeting),

                () -> assertThat(foundParticipants)
                        .usingRecursiveComparison()
                        .ignoringFields("user", "meeting", "tardyCount")
                        .isEqualTo(List.of(foundParticipant1, foundParticipant2)),

                () -> assertThat(foundTardyCounts)
                        .usingRecursiveComparison()
                        .isEqualTo(List.of(1L, 0L)),

                () -> assertThat(foundUsers)
                        .usingRecursiveComparison()
                        .isEqualTo(List.of(user1, user2))
        );
    }

    @Test
    void meetingsWithTardyCount() {
        // given
        final Meeting meeting = MORAGORA.create();
        final User user1 = PHILLZ.create();
        final User user2 = AZPI.create();
        final Participant participant1 = dataSupport.saveParticipant(user1, meeting, true);
        final Participant participant2 = dataSupport.saveParticipant(user2, meeting, false);
        final Event event = EVENT1.create(meeting);

        dataSupport.saveMeeting(meeting);
        dataSupport.saveUsers(user1, user2);
        dataSupport.saveEvent(event);
        dataSupport.saveAttendance(participant1, event, Status.TARDY);
        dataSupport.saveAttendance(participant2, event, Status.PRESENT);

        // when
        final Meeting user1Meeting = compositionRepository.meetingsWithTardyCount(user1.getId()).get(0);
        final Meeting user2Meeting = compositionRepository.meetingsWithTardyCount(user2.getId()).get(0);

        final Participant user1Participant = user1Meeting.getParticipants().get(0);
        final Participant user2Participant = user2Meeting.getParticipants().get(0);

        // then
        assertAll(
                assertMeetingSame(user1Meeting, meeting),
                assertParticipantSame(user1Participant, participant1),
                assertEquals(user1Participant.getTardyCount(), 1L),
                assertEquals(user2Participant.getTardyCount(), 0L)
        );
    }

    private Executable assertMeetingSame(final Meeting actualMeeting,
                                         final Meeting expectedMeeting) {
        return () -> assertThat(actualMeeting)
                .usingRecursiveComparison()
                .ignoringFields("participants")
                .isEqualTo(expectedMeeting);
    }

    private Executable assertParticipantSame(final Participant actualParticipant,
                                             final Participant expectedParticipant) {
        return () -> assertThat(actualParticipant)
                .usingRecursiveComparison()
                .ignoringFields("user", "meeting", "tardyCount")
                .isEqualTo(expectedParticipant);
    }

    private <T> Executable assertEquals(final T actual, final T expected) {
        return () -> assertThat(actual).isEqualTo(expected);
    }
}