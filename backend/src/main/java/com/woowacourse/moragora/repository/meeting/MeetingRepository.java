package com.woowacourse.moragora.repository.meeting;

import com.woowacourse.moragora.entity.Meeting;
import java.util.Optional;

public interface MeetingRepository {

    Meeting save(final Meeting meeting);

    Optional<Meeting> findById(final Long id);
}
