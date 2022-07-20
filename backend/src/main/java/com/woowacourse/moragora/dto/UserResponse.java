package com.woowacourse.moragora.dto;

import com.woowacourse.moragora.entity.Status;
import com.woowacourse.moragora.entity.user.User;
import java.util.Locale;
import lombok.Getter;

@Getter
public class UserResponse {

    private final Long id;
    private final String email;
    private final String nickname;
    private final String attendanceStatus;
    private final int tardyCount;

    public UserResponse(final Long id,
                        final String email,
                        final String nickname,
                        final Status attendanceStatus,
                        final int tardyCount) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.attendanceStatus = attendanceStatus.name()
                .toLowerCase(Locale.ROOT);
        this.tardyCount = tardyCount;
    }

    public UserResponse(final Long id, final String email, final String nickname, final String attendanceStatus,
                        final int tardyCount) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.attendanceStatus = attendanceStatus;
        this.tardyCount = tardyCount;
    }

    public static UserResponse of(final User foundUser, final Status attendanceStatus, final int tardyCount) {
        return new UserResponse(foundUser.getId(), foundUser.getEmail(), foundUser.getNickname(),
                attendanceStatus.name().toLowerCase(Locale.ROOT), tardyCount);
    }
}
