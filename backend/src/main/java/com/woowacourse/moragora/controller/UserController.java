package com.woowacourse.moragora.controller;

import com.woowacourse.auth.support.Authentication;
import com.woowacourse.auth.support.AuthenticationPrincipal;
import com.woowacourse.moragora.dto.EmailCheckResponse;
import com.woowacourse.moragora.dto.NicknameRequest;
import com.woowacourse.moragora.dto.UserRequest;
import com.woowacourse.moragora.dto.UserResponse;
import com.woowacourse.moragora.dto.UsersResponse;
import com.woowacourse.moragora.service.UserService;
import java.net.URI;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Void> signUp(@RequestBody @Valid final UserRequest userRequest) {
        final Long id = userService.create(userRequest);
        return ResponseEntity.created(URI.create("/users/" + id)).build();
    }

    @GetMapping("/check-email")
    public ResponseEntity<EmailCheckResponse> checkEmail(@RequestParam final String email) {
        final EmailCheckResponse response = userService.isEmailExist(email);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<UsersResponse> search(@RequestParam final String keyword) {
        final UsersResponse response = userService.searchByKeyword(keyword);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    @Authentication
    public ResponseEntity<UserResponse> findMyInfo(@AuthenticationPrincipal final Long id) {
        UserResponse response = userService.findById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/me/nickname")
    @Authentication
    public ResponseEntity<Void> changeMyNickname(@RequestBody @Valid final NicknameRequest nicknameRequest,
                                                 @AuthenticationPrincipal final Long id) {
        userService.updateNickname(nicknameRequest, id);
        return ResponseEntity.noContent().build();
    }
}
