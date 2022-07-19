package com.woowacourse.moragora.entity;

import java.time.LocalDate;
import javax.persistence.Entity;
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

    private boolean isTardy;

    public Attendance(final Participant participant,
                      final LocalDate attendanceDate,
                      final boolean isTardy) {
        this.participant = participant;
        this.attendanceDate = attendanceDate;
        this.isTardy = isTardy;
    }
}
