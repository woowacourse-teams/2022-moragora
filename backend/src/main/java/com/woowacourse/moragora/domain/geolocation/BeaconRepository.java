package com.woowacourse.moragora.domain.geolocation;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface BeaconRepository extends Repository<Beacon, Long> {

    List<Beacon> saveAll(final Iterable<Beacon> beacons);

    List<Beacon> findAllByMeetingId(final Long meetingId);

    Optional<Beacon> findById(final Long id);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("delete from Beacon b where b.meeting.id = :meetingId")
    void deleteByMeetingId(@Param("meetingId") final Long meetingId);
}
