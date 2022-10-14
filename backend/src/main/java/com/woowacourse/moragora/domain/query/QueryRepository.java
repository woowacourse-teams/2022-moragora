package com.woowacourse.moragora.domain.query;

import com.woowacourse.moragora.domain.meeting.Meeting;
import com.woowacourse.moragora.domain.participant.Participant;
import com.woowacourse.moragora.domain.participant.ParticipantAndCount;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface QueryRepository extends Repository<Meeting, Long> {

    @Query("select p as participant, "
            +   "(select count(a) "
            +   "from Attendance a "
            +   "where a.participant.id = p.id and a.status = 'TARDY' and a.disabled = false) "
            +   "as tardyCount "
            + "from Participant p join fetch p.user "
            + "where p in :participants")
    List<ParticipantAndCount> countParticipantsTardy(@Param("participants") final List<Participant> participants);
}
