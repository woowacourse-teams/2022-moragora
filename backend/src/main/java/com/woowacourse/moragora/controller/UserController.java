package com.woowacourse.moragora.controller;

import com.woowacourse.moragora.dto.UserRequest;
import com.woowacourse.moragora.service.UserService;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Void> signUp(@RequestBody final UserRequest userRequest) {
        final Long id = userService.create(userRequest);
        return ResponseEntity.created(URI.create("/users/" + id)).build();
    }
}
