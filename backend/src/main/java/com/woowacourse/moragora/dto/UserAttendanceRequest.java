package com.woowacourse.moragora.dto;

import com.woowacourse.moragora.entity.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
public class UserAttendanceRequest {

    private Status attendanceStatus;

    public UserAttendanceRequest(final Status attendanceStatus) {
        this.attendanceStatus = attendanceStatus;
    }
}
