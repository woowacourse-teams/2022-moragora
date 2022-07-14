package com.woowacourse.moragora.dto;

import com.woowacourse.moragora.entity.Attendance;
import com.woowacourse.moragora.entity.user.User;
import lombok.Getter;

@Getter
public class UserResponse {

    private final Long id;
    private final String email;
    private final String nickname;
    private final int tardyCount;

    public UserResponse(final Long id,
                        final String email,
                        final String nickname,
                        final int tardyCount) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.tardyCount = tardyCount;
    }

    public static UserResponse from(final Attendance attendance) {
        final User user = attendance.getUser();
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getNickname(),
                attendance.getTardyCount()
        );
    }
}
