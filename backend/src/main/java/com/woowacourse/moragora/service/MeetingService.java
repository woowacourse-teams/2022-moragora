package com.woowacourse.moragora.service;

import com.woowacourse.moragora.dto.MeetingResponse;
import com.woowacourse.moragora.entity.Meeting;
import com.woowacourse.moragora.repository.MeetingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MeetingService {

    private final MeetingRepository meetingRepository;

    public MeetingService(final MeetingRepository meetingRepository) {
        this.meetingRepository = meetingRepository;
    }

    public MeetingResponse findById(final Long id) {
        final Meeting meeting = meetingRepository.findById(id).get();
        return new MeetingResponse(meeting.getId(), meeting.getMeetingCount());
    }
}
