package com.woowacourse.moragora.presentation.auth;

import com.woowacourse.moragora.application.auth.AuthService;
import com.woowacourse.moragora.dto.request.auth.EmailRequest;
import com.woowacourse.moragora.dto.request.user.LoginRequest;
import com.woowacourse.moragora.dto.response.auth.ExpiredTimeResponse;
import com.woowacourse.moragora.dto.response.user.LoginResponse;
import javax.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final AuthService authService;

    public AuthController(final AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody final LoginRequest loginRequest) {
        final LoginResponse loginResponse = authService.createToken(loginRequest);
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/login/oauth2/google")
    public ResponseEntity<LoginResponse> loginWithGoogle(@RequestParam("code") final String code) {
        final LoginResponse loginResponse = authService.loginWithGoogle(code);
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/emails/send")
    public ResponseEntity<ExpiredTimeResponse> sendEmail(@RequestBody final EmailRequest emailRequest,
                                                         final HttpSession session) {
        final ExpiredTimeResponse response = authService.sendAuthCode(emailRequest, session);
        return ResponseEntity.ok(response);
    }
}
