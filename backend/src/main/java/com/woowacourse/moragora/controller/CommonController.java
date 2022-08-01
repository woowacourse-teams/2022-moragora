package com.woowacourse.moragora.controller;

import com.woowacourse.moragora.dto.ServerTimeResponse;
import com.woowacourse.moragora.service.CommonService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommonController {

    private final CommonService commonService;

    public CommonController(final CommonService commonService) {
        this.commonService = commonService;
    }

    @GetMapping("/server-time")
    public ResponseEntity<ServerTimeResponse> showServerTime() {
        final ServerTimeResponse response = commonService.getServerTime();
        return ResponseEntity.ok(response);
    }
}
