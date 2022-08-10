package com.woowacourse.moragora.dto;

import com.woowacourse.moragora.entity.Status;
import com.woowacourse.moragora.entity.user.User;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ParticipantResponse {

    private final Long id;
    private final String email;
    private final String nickname;
    private final String attendanceStatus;
    private final int tardyCount;

    private ParticipantResponse(final Long id,
                                final String email,
                                final String nickname,
                                final String attendanceStatus,
                                final int tardyCount) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.attendanceStatus = attendanceStatus.toLowerCase();
        this.tardyCount = tardyCount;
    }

    public static ParticipantResponse of(final User foundUser, final Status attendanceStatus, final int tardyCount) {
        return new ParticipantResponse(
                foundUser.getId(),
                foundUser.getEmail(),
                foundUser.getNickname(),
                attendanceStatus.name(),
                tardyCount);
    }
}
