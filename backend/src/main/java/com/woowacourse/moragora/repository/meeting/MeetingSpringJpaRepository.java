package com.woowacourse.moragora.repository.meeting;

import com.woowacourse.moragora.entity.Meeting;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingSpringJpaRepository extends JpaRepository<Meeting, Long>, MeetingRepository {

    Meeting save(final Meeting meeting);

    Optional<Meeting> findById(final Long id);
}
