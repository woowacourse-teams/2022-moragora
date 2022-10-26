package com.woowacourse.moragora.domain.participant;

import lombok.Getter;

@Getter
public class TardyCountDto {

    private Long participantId;
    private Long tardyCount;

    public TardyCountDto(final Long participantId, final Long tardyCount) {
        this.participantId = participantId;
        this.tardyCount = tardyCount;
    }
}
