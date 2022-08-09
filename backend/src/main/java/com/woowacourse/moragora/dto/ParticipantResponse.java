package com.woowacourse.moragora.dto;

import com.woowacourse.moragora.entity.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ParticipantResponse {

    private final Long id;
    private final String email;
    private final String nickname;
    private final int tardyCount;
    private final Boolean isMaster;

    @Builder
    public ParticipantResponse(final Long id,
                               final String email,
                               final String nickname,
                               final int tardyCount,
                               final boolean isMaster) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.tardyCount = tardyCount;
        this.isMaster = isMaster;
    }

    public static ParticipantResponse of(final User foundUser, final int tardyCount, final boolean isMaster) {
        return new ParticipantResponse(
                foundUser.getId(),
                foundUser.getEmail(),
                foundUser.getNickname(),
                tardyCount,
                isMaster);
    }
}
