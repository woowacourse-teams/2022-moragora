package com.woowacourse.moragora.entity;

import java.time.LocalDate;
import javax.persistence.Column;
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
@Table(name = "master")
@NoArgsConstructor
@Getter
public class Master {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "participant_id")
    private Participant participant;

    @Column(nullable = false)
    private LocalDate appointedDate;

    public Master(final Participant participant, final LocalDate appointedDate) {
        this.participant = participant;
        this.appointedDate = appointedDate;
    }
}
