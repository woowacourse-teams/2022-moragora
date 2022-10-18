package com.woowacourse.moragora.presentation.auth;

import static com.woowacourse.moragora.constant.SessionAttributeNames.AUTH_CODE;
import static com.woowacourse.moragora.constant.SessionAttributeNames.VERIFIED_EMAIL;
import static org.springframework.http.HttpHeaders.SET_COOKIE;

import com.woowacourse.moragora.application.auth.AuthService;
import com.woowacourse.moragora.domain.auth.AuthCode;
import com.woowacourse.moragora.dto.request.auth.EmailRequest;
import com.woowacourse.moragora.dto.request.auth.EmailVerifyRequest;
import com.woowacourse.moragora.dto.request.user.LoginRequest;
import com.woowacourse.moragora.dto.response.auth.ExpiredTimeResponse;
import com.woowacourse.moragora.dto.response.user.LoginResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

@RestController
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenCookieProvider refreshTokenCookieProvider;

    public AuthController(final AuthService authService,
                          final RefreshTokenCookieProvider refreshTokenCookieProvider) {
        this.authService = authService;
        this.refreshTokenCookieProvider = refreshTokenCookieProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(final HttpServletResponse response,
                                               @RequestBody final LoginRequest loginRequest) {
        final TokenResponse tokenResponse = authService.login(loginRequest);
        final ResponseCookie responseCookie = refreshTokenCookieProvider.create(tokenResponse.getRefreshToken());
        response.addHeader(SET_COOKIE, responseCookie.toString());

        final LoginResponse loginResponse = new LoginResponse(tokenResponse.getAccessToken());
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/login/oauth2/google")
    public ResponseEntity<LoginResponse> loginWithGoogle(final HttpServletResponse response,
                                                         @RequestParam("code") final String code) {
        final TokenResponse tokenResponse = authService.loginWithGoogle(code);
        final ResponseCookie responseCookie = refreshTokenCookieProvider.create(tokenResponse.getRefreshToken());
        response.addHeader(SET_COOKIE, responseCookie.toString());

        final LoginResponse loginResponse = new LoginResponse(tokenResponse.getAccessToken());
        return ResponseEntity.ok(loginResponse);
    }

    @GetMapping("/token/refresh")
    public ResponseEntity<LoginResponse> refreshTokens(final HttpServletRequest request,
                                                       final HttpServletResponse response) {
        final String refreshToken = refreshTokenCookieProvider.extractRefreshToken(request.getCookies());
        final TokenResponse tokenResponse = authService.refreshTokens(refreshToken);
        final ResponseCookie responseCookie = refreshTokenCookieProvider.create(tokenResponse.getRefreshToken());
        response.addHeader(SET_COOKIE, responseCookie.toString());
        final LoginResponse loginResponse = new LoginResponse(tokenResponse.getAccessToken());
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/token/logout")
    public ResponseEntity<Void> logout(final HttpServletRequest request,
                                       final HttpServletResponse response) {
        final String refreshToken = refreshTokenCookieProvider.extractRefreshToken(request.getCookies());
        final ResponseCookie responseCookie = refreshTokenCookieProvider.createInvalidCookie();
        response.addHeader(SET_COOKIE, responseCookie.toString());
        authService.removeRefreshToken(refreshToken);
        return ResponseEntity.noContent().build();
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
