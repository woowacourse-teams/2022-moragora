package com.woowacourse.moragora.dto;

import com.woowacourse.moragora.entity.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserAttendanceRequest {

    private Status attendanceStatus;

    public UserAttendanceRequest(final Status attendanceStatus) {
        this.attendanceStatus = attendanceStatus;
    }
}
