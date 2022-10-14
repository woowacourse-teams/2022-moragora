package com.woowacourse.moragora.domain.geolocation;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface BeaconRepository extends Repository<Beacon, Long> {

    Beacon save(final Beacon beacon);

    List<Beacon> findAllByMeetingId(final Long id);

    Optional<Beacon> findById(final Long id);
}
