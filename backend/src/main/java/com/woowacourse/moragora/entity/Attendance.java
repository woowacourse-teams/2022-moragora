package com.woowacourse.moragora.entity;

import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "attendance")
@NoArgsConstructor
@Getter
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "participant_id")
    private Participant participant;

    private LocalDate attendanceDate;

    @Enumerated(EnumType.STRING)
    private Status status;

    public Attendance(final Participant participant, final LocalDate attendanceDate, final Status status) {
        this.participant = participant;
        this.attendanceDate = attendanceDate;
        this.status = status;
    }

    public void changeAttendanceStatus(Status status) {
        this.status = status;
    }
}
