package com.woowacourse.auth.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

// TODO Delete this project

@RestController
public class TestController {

    @GetMapping("/test-run-ex")
    public ResponseEntity<Void> login() {
        throw new RuntimeException("에러");
    }
}
