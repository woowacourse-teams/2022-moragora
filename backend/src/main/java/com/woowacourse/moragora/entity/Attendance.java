package com.woowacourse.moragora.entity;

import com.woowacourse.moragora.entity.user.User;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
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
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private int tardyCount;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "meeting_id")
    private Meeting meeting;

    public Attendance(final User user, final Meeting meeting) {
        this.user = user;
        this.meeting = meeting;
        this.tardyCount = 0;
    }

    public void increaseTardyCount() {
        tardyCount++;
    }
}
