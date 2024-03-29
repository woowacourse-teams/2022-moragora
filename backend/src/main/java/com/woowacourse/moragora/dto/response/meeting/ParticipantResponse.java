package com.woowacourse.moragora.dto.response.meeting;

import com.woowacourse.moragora.domain.participant.Participant;
import com.woowacourse.moragora.domain.user.User;
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
                               final long tardyCount,
                               final boolean isMaster) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.tardyCount = (int) tardyCount;
        this.isMaster = isMaster;
    }

    public static ParticipantResponse from(final Participant participant) {
        final User user = participant.getUser();

        return new ParticipantResponse(
                user.getId(),
                user.getEmail(),
                user.getNickname(),
                participant.getTardyCount(),
                participant.getIsMaster());
    }
}
