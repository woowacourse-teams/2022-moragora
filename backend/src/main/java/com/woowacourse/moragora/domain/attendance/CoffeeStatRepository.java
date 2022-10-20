package com.woowacourse.moragora.domain.attendance;

import com.woowacourse.moragora.domain.participant.Participant;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CoffeeStatRepository extends JpaRepository<Attendance, Long> {

    @Query("select a from Attendance a "
            + "where a.disabled = false and a.status = 'TARDY' "
            + "and a.participant in :participants "
            + "order by a.event.date asc ")
    List<Attendance> findCoffeeStatsLimitParticipant(final Pageable pageable,
                                                     @Param("participants") final List<Participant> participants);
}
