package com.woowacourse.moragora.entity;

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
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "event")
@NoArgsConstructor
@Getter
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private LocalTime entranceTime;

    @Column(nullable = false)
    private LocalTime leaveTime;

    @ManyToOne
    @JoinColumn(name = "meeting_id")
    private Meeting meeting;

    @Builder
    public Event(final Long id,
                 final LocalDate date,
                 final LocalTime entranceTime,
                 final LocalTime leaveTime,
                 final Meeting meeting) {
        this.id = id;
        this.date = date;
        this.entranceTime = entranceTime;
        this.leaveTime = leaveTime;
        this.meeting = meeting;
    }

    public Event(final LocalDate date, final LocalTime entranceTime, final LocalTime leaveTime, final Meeting meeting) {
        this(null, date, entranceTime, leaveTime, meeting);
    }

    public boolean isSameDate(final LocalDate date) {
        return this.date.isEqual(date);
    }
}
