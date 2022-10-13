package com.woowacourse.moragora.domain.meeting;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface MeetingRepository extends Repository<Meeting, Long> {

    Meeting save(final Meeting meeting);

    Boolean existsById(final Long id);

    Optional<Meeting> findById(final Long id);

    @Query("select m from Meeting m join fetch m.participants p where m.id = :id")
    Optional<Meeting> findMeetingAndParticipantsById(@Param("id") final Long id);

    @Query("select m "
            + " from Meeting m "
            + " join fetch m.participants p "
            + " where p.user.id = :userId")
    List<Meeting> findMeetingParticipantsByUserId(@Param("userId") Long userId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("delete from Meeting m where m.id = :id")
    void deleteById(@Param("id") final Long id);
}
