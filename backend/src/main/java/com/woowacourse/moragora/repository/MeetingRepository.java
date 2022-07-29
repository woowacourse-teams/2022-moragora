package com.woowacourse.moragora.repository;

import com.woowacourse.moragora.entity.Meeting;
import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface MeetingRepository extends Repository<Meeting, Long> {

    Meeting save(final Meeting meeting);

    Optional<Meeting> findById(final Long id);
}
