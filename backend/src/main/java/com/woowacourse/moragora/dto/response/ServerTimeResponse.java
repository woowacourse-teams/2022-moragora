package com.woowacourse.moragora.dto.response;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class ServerTimeResponse {

    private final long serverTime;

    private ServerTimeResponse(final long serverTime) {
        this.serverTime = serverTime;
    }

    public ServerTimeResponse(final LocalDateTime serverTime) {
        this(toTimestamp(serverTime));
    }

    private static long toTimestamp(final LocalDateTime serverTime) {
        return Timestamp.valueOf(serverTime).getTime();
    }
}
