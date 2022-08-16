package com.woowacourse.moragora.dto;

import java.util.List;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class AttendancesResponse {

    private final List<AttendanceResponse> users;

    public AttendancesResponse(final List<AttendanceResponse> users) {
        this.users = users;
    }
}
