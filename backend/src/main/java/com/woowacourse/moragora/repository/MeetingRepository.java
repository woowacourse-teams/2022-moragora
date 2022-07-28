package com.woowacourse.moragora.repository;

import com.woowacourse.moragora.entity.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingRepository extends JpaRepository<Meeting, Long> {
}
