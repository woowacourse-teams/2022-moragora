package com.woowacourse.moragora.service;

import com.woowacourse.moragora.dto.ServerTimeResponse;
import com.woowacourse.moragora.support.ServerTimeManager;
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
