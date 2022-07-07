package com.woowacourse.moragora.entity;

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
    private int meetingCount;

    public Meeting(final Long id, final int meetingCount) {
        this.id = id;
        this.meetingCount = meetingCount;
    }
}
