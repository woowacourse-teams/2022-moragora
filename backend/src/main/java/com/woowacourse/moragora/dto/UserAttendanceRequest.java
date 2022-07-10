package com.woowacourse.moragora.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserAttendanceRequest {

    private Long id;
    private Boolean isTardy;

    public UserAttendanceRequest(final Long id, final Boolean isTardy) {
        this.id = id;
        this.isTardy = isTardy;
    }
}
