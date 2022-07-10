package com.woowacourse.moragora.entity;

import java.time.LocalDate;
import java.time.LocalTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "meeting")
@NoArgsConstructor
@Getter
public class Meeting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int attendanceCount;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private LocalTime entranceTime;

    @Column(nullable = false)
    private LocalTime leaveTime;

    public Meeting(final String name,
                   final LocalDate startDate,
                   final LocalDate endDate,
                   final LocalTime entranceTime,
                   final LocalTime leaveTime) {
        this.name = name;
        this.attendanceCount = 0;
        this.startDate = startDate;
        this.endDate = endDate;
        this.entranceTime = entranceTime;
        this.leaveTime = leaveTime;
    }

    public void increaseMeetingCount() {
        attendanceCount++;
    }
}
