package com.woowacourse.moragora.presentation;

import com.woowacourse.moragora.application.UserService;
import com.woowacourse.moragora.constant.SessionAttributeNames;
import com.woowacourse.moragora.dto.request.user.NicknameRequest;
import com.woowacourse.moragora.dto.request.user.PasswordRequest;
import com.woowacourse.moragora.dto.request.user.UserDeleteRequest;
import com.woowacourse.moragora.dto.request.user.UserRequest;
import com.woowacourse.moragora.dto.response.user.UserResponse;
import com.woowacourse.moragora.dto.response.user.UsersResponse;
import com.woowacourse.moragora.presentation.auth.Authentication;
import com.woowacourse.moragora.presentation.auth.AuthenticationPrincipal;
import java.net.URI;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Void> signUp(@RequestBody @Valid final UserRequest userRequest,
                                       @SessionAttribute(name = SessionAttributeNames.VERIFIED_EMAIL,
                                               required = false) final String verifiedEmail,
                                       final HttpSession httpSession) {
        final Long id = userService.create(userRequest, verifiedEmail);
        httpSession.invalidate();
        return ResponseEntity.created(URI.create("/users/" + id)).build();
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

    @PutMapping("/me/password")
    @Authentication
    public ResponseEntity<Void> changeMyPassword(@RequestBody @Valid final PasswordRequest passwordRequest,
                                                 @AuthenticationPrincipal final Long id) {
        userService.updatePassword(passwordRequest, id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMe(@RequestBody @Valid final UserDeleteRequest userDeleteRequest,
                                         @AuthenticationPrincipal final Long id) {
        userService.delete(userDeleteRequest, id);
        return ResponseEntity.noContent().build();
    }
}
