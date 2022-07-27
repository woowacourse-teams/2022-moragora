package com.woowacourse.moragora.repository.meeting;

import com.woowacourse.moragora.entity.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingSpringJpaRepository extends JpaRepository<Meeting, Long>, MeetingRepository {
}
