package com.woowacourse.moragora.controller;

import com.woowacourse.moragora.dto.ServerTimeResponse;
import com.woowacourse.moragora.service.CommonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class CommonController {

    private final CommonService commonService;

    public CommonController(final CommonService commonService) {
        this.commonService = commonService;
    }

    @GetMapping("/server-time")
    public ResponseEntity<ServerTimeResponse> showServerTime() {
        final Thread thread = Thread.currentThread();
//        log.info("t-id = {}, t-name = {}", thread.getId(), thread.getName());
        delay(1000);
        final ServerTimeResponse response = commonService.getServerTime();
        return ResponseEntity.ok(response);
    }

    private void delay(final int seconds) {
        try {
            Thread.sleep(seconds);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
