package com.woowacourse.moragora.dto;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserAttendancesRequest {

    private List<UserAttendanceRequest> users;

    public UserAttendancesRequest(final List<UserAttendanceRequest> users) {
        this.users = users;
    }
}
