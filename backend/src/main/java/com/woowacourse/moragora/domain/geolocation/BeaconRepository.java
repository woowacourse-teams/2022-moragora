package com.woowacourse.moragora.domain.geolocation;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface BeaconRepository extends Repository<Beacon, Long> {

    List<Beacon> saveAll(final Iterable<Beacon> beacons);

    List<Beacon> findAllByMeetingId(final Long meetingId);

    Optional<Beacon> findById(final Long id);
}
