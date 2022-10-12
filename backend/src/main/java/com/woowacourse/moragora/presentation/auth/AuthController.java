package com.woowacourse.moragora.presentation.auth;

import com.woowacourse.moragora.application.auth.AuthService;
import com.woowacourse.moragora.dto.request.user.LoginRequest;
import com.woowacourse.moragora.dto.response.user.LoginResponse;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
        final LoginResult loginResult = authService.login(loginRequest);
        final ResponseCookie responseCookie = refreshTokenCookieProvider.create(loginResult.getRefreshToken());
        response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());

        final LoginResponse loginResponse = new LoginResponse(loginResult.getAccessToken());
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/login/oauth2/google")
    public ResponseEntity<LoginResponse> loginWithGoogle(@RequestParam("code") final String code) {
        final LoginResponse loginResponse = authService.loginWithGoogle(code);
        return ResponseEntity.ok(loginResponse);
    }
}
