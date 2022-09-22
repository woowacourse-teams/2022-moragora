package com.woowacourse.moragora.presentation;

import com.woowacourse.moragora.dto.response.ServerTimeResponse;
import com.woowacourse.moragora.application.CommonService;
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
