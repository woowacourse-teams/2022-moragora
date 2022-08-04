package com.woowacourse.moragora.service;

import com.woowacourse.moragora.dto.EventsRequest;
import com.woowacourse.moragora.entity.Event;
import com.woowacourse.moragora.entity.Meeting;
import com.woowacourse.moragora.exception.meeting.MeetingNotFoundException;
import com.woowacourse.moragora.repository.EventRepository;
import com.woowacourse.moragora.repository.MeetingRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final MeetingRepository meetingRepository;

    public EventService(final EventRepository eventRepository,
                        final MeetingRepository meetingRepository) {
        this.eventRepository = eventRepository;
        this.meetingRepository = meetingRepository;
    }

    public void save(final EventsRequest request, final Long meetingId) {
        final Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(MeetingNotFoundException::new);
        final List<Event> events = request.toEntities(meeting);
        eventRepository.saveAll(events);
    }
}
