package com.woowacourse.moragora.repository.meeting;

import com.woowacourse.moragora.entity.Meeting;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface MeetingSpringJpaRepository extends JpaRepository<Meeting, Long>, MeetingRepository {

    @Transactional
    Meeting save(final Meeting meeting);

    Optional<Meeting> findById(final Long id);
}
