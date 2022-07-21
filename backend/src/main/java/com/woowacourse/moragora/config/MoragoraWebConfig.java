package com.woowacourse.moragora.config;

import com.woowacourse.moragora.service.closingstrategy.RealTimeChecker;
import com.woowacourse.moragora.service.closingstrategy.TimeChecker;
import com.woowacourse.moragora.util.CurrentDateTime;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MoragoraWebConfig {

    @Bean
    public TimeChecker timeChecker(CurrentDateTime currentDateTime) {
        return new RealTimeChecker(currentDateTime);
    }

    @Bean
    public CurrentDateTime serverDateTime() {
        return new CurrentDateTime();
    }
}
