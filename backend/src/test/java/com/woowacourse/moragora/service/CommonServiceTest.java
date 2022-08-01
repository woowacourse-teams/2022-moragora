package com.woowacourse.moragora.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.woowacourse.moragora.dto.ServerTimeResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CommonServiceTest {

    @Autowired
    private CommonService commonService;

    @DisplayName("현재 서버 시간을 timestamp 형식으로 조회한다")
    @Test
    void getServerTime() {
        // given, when
        final ServerTimeResponse response = commonService.getServerTime();

        // then
        assertThat(response.getServerTime()).isNotNull();
    }

}
