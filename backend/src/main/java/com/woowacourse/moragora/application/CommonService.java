package com.woowacourse.moragora.application;

import com.woowacourse.moragora.dto.response.ServerTimeResponse;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;

@Service
public class CommonService {

    private final ServerTimeManager serverTimeManager;

    public CommonService(final ServerTimeManager serverTimeManager) {
        this.serverTimeManager = serverTimeManager;
    }

    public ServerTimeResponse getServerTime() {
        final LocalDateTime now = serverTimeManager.getDateAndTime();
        return new ServerTimeResponse(now);
    }
}
