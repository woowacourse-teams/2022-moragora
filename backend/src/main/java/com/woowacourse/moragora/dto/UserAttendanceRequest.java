package com.woowacourse.moragora.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserAttendanceRequest {

    private Long id;
    private Boolean isAbsent;

    public UserAttendanceRequest(final Long id, final Boolean isAbsent) {
        this.id = id;
        this.isAbsent = isAbsent;
    }
}
