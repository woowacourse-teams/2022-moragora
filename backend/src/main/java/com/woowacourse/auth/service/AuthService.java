package com.woowacourse.auth.service;

import com.woowacourse.auth.dto.LoginRequest;
import com.woowacourse.auth.dto.LoginResponse;
import com.woowacourse.auth.exception.LoginFailException;
import com.woowacourse.auth.support.JwtTokenProvider;
import com.woowacourse.moragora.entity.user.User;
import com.woowacourse.moragora.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    public AuthService(final JwtTokenProvider jwtTokenProvider, final UserRepository userRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
    }

    public LoginResponse createToken(final LoginRequest loginRequest) {
        final User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new LoginFailException());

        user.checkPassword(loginRequest.getPassword());
        final String accessToken = jwtTokenProvider.createToken(String.valueOf(user.getId()));
        return new LoginResponse(accessToken);
    }
}
