package com.woowacourse.moragora.presentation.auth;

import static com.woowacourse.moragora.constant.SessionAttributeNames.AUTH_CODE;
import static com.woowacourse.moragora.constant.SessionAttributeNames.VERIFIED_EMAIL;

import com.woowacourse.moragora.application.auth.AuthService;
import com.woowacourse.moragora.domain.auth.AuthCode;
import com.woowacourse.moragora.dto.request.auth.EmailRequest;
import com.woowacourse.moragora.dto.request.auth.EmailVerifyRequest;
import com.woowacourse.moragora.dto.request.user.LoginRequest;
import com.woowacourse.moragora.dto.response.auth.ExpiredTimeResponse;
import com.woowacourse.moragora.dto.response.user.LoginResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

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

    @PostMapping("/email/send")
    public ResponseEntity<ExpiredTimeResponse> sendEmail(@RequestBody @Valid final EmailRequest emailRequest,
                                                         final HttpSession session) {
        final AuthCode authCode = authService.sendAuthCode(emailRequest);

        session.setAttribute(AUTH_CODE, authCode);
        session.removeAttribute(VERIFIED_EMAIL);

        return ResponseEntity.ok(new ExpiredTimeResponse(authCode.getExpiredTime()));
    }

    @PostMapping("/email/verify")
    public ResponseEntity<Void> verifyCode(@RequestBody @Valid final EmailVerifyRequest emailVerifyRequest,
                                           @SessionAttribute(name = AUTH_CODE, required = false) final AuthCode authCode,
                                           final HttpSession session) {
        final String email = authService.verifyAuthCode(emailVerifyRequest, authCode);
        session.setAttribute(VERIFIED_EMAIL, email);
        return ResponseEntity.noContent().build();
    }
}
