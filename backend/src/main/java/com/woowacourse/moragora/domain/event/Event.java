package com.woowacourse.moragora.domain.event;

import com.woowacourse.moragora.domain.meeting.Meeting;
import com.woowacourse.moragora.exception.meeting.IllegalEntranceLeaveTimeException;
import java.time.LocalDate;
import java.time.LocalTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "event")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Include
    private Long id;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    @ManyToOne
    @JoinColumn(name = "meeting_id")
    private Meeting meeting;

    @Builder
    public Event(final Long id,
                 final LocalDate date,
                 final LocalTime startTime,
                 final LocalTime endTime,
                 final Meeting meeting) {
        validateStartEndTime(startTime, endTime);
        this.id = id;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.meeting = meeting;
    }

    public Event(final LocalDate date, final LocalTime startTime, final LocalTime endTime, final Meeting meeting) {
        this(null, date, startTime, endTime, meeting);
    }

    public boolean isSameDate(final Event other) {
        return this.date.isEqual(other.date);
    }

    public boolean isSameDate(final LocalDate date) {
        return this.date.isEqual(date);
    }

    public boolean isSameMeeting(final Meeting meeting) {
        return this.meeting.equals(meeting);
    }

    public void updateTime(final LocalTime startTime, final LocalTime endTime) {
        validateStartEndTime(startTime, endTime);
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public void updateTime(final Event other) {
        validateStartEndTime(other.startTime, other.endTime);
        this.startTime = other.startTime;
        this.endTime = other.endTime;
    }

    public boolean isDateBefore(final LocalDate date) {
        return this.date.isBefore(date);
    }

    private void validateStartEndTime(final LocalTime entranceTime, final LocalTime leaveTime) {
        if (entranceTime.isAfter(leaveTime)) {
            throw new IllegalEntranceLeaveTimeException();
        }
    }
}
