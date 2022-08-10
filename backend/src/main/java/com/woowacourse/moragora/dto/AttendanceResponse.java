package com.woowacourse.moragora.dto;

import com.woowacourse.moragora.entity.Attendance;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class AttendanceResponse {

    private final Long id;
    private final String nickname;
    private final String attendanceStatus;

    public AttendanceResponse(final Long id, final String nickname, final String attendanceStatus) {
        this.id = id;
        this.nickname = nickname;
        this.attendanceStatus = attendanceStatus;
    }

    public static AttendanceResponse from(final Attendance attendance) {
        return new AttendanceResponse(
                attendance.getParticipant().getUser().getId(),
                attendance.getParticipant().getUser().getNickname(),
                attendance.getStatus().name()
        );
    }
}
