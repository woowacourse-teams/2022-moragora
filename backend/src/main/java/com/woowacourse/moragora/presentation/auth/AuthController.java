package com.woowacourse.moragora.presentation.auth;

import com.woowacourse.moragora.application.auth.AuthService;
import com.woowacourse.moragora.dto.request.user.LoginRequest;
import com.woowacourse.moragora.dto.response.user.LoginResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
        final TokenResponse tokenResponse = authService.login(loginRequest);
        final ResponseCookie responseCookie = refreshTokenCookieProvider.create(tokenResponse.getRefreshToken());
        response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());

        final LoginResponse loginResponse = new LoginResponse(tokenResponse.getAccessToken());
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/login/oauth2/google")
    public ResponseEntity<LoginResponse> loginWithGoogle(final HttpServletResponse response,
                                                         @RequestParam("code") final String code) {
        final TokenResponse tokenResponse = authService.loginWithGoogle(code);
        final ResponseCookie responseCookie = refreshTokenCookieProvider.create(tokenResponse.getRefreshToken());
        response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());

        final LoginResponse loginResponse = new LoginResponse(tokenResponse.getAccessToken());
        return ResponseEntity.ok(loginResponse);
    }

    @GetMapping("/token/refresh")
    public ResponseEntity<LoginResponse> refreshTokens(final HttpServletRequest request,
                                                       final HttpServletResponse response) {
        final String refreshToken = refreshTokenCookieProvider.extractRefreshToken(request.getCookies());
        final TokenResponse tokenResponse = authService.refreshTokens(refreshToken);
        final ResponseCookie responseCookie = refreshTokenCookieProvider.create(tokenResponse.getRefreshToken());
        response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());

        final LoginResponse loginResponse = new LoginResponse(tokenResponse.getAccessToken());
        return ResponseEntity.ok(loginResponse);
    }
}
