package com.woowacourse.moragora.application.auth;

import static com.woowacourse.moragora.domain.user.Provider.CHECKMATE;
import static com.woowacourse.moragora.domain.user.Provider.GOOGLE;

import com.woowacourse.moragora.application.ServerTimeManager;
import com.woowacourse.moragora.domain.participant.Participant;
import com.woowacourse.moragora.domain.participant.ParticipantRepository;
import com.woowacourse.moragora.domain.user.User;
import com.woowacourse.moragora.domain.user.UserRepository;
import com.woowacourse.moragora.domain.user.password.RawPassword;
import com.woowacourse.moragora.dto.request.user.LoginRequest;
import com.woowacourse.moragora.dto.response.user.GoogleProfileResponse;
import com.woowacourse.moragora.exception.InvalidTokenException;
import com.woowacourse.moragora.exception.participant.ParticipantNotFoundException;
import com.woowacourse.moragora.exception.user.AuthenticationFailureException;
import com.woowacourse.moragora.infrastructure.GoogleClient;
import com.woowacourse.moragora.presentation.auth.TokenResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenProvider refreshTokenProvider;
    private final ServerTimeManager serverTimeManager;
    private final GoogleClient googleClient;
    private final UserRepository userRepository;
    private final ParticipantRepository participantRepository;

    public AuthService(final JwtTokenProvider jwtTokenProvider,
                       final RefreshTokenProvider refreshTokenProvider,
                       final ServerTimeManager serverTimeManager,
                       final GoogleClient googleClient,
                       final UserRepository userRepository,
                       final ParticipantRepository participantRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.refreshTokenProvider = refreshTokenProvider;
        this.serverTimeManager = serverTimeManager;
        this.googleClient = googleClient;
        this.userRepository = userRepository;
        this.participantRepository = participantRepository;
    }

    public TokenResponse login(final LoginRequest loginRequest) {
        final User user = userRepository.findByEmailAndProvider(loginRequest.getEmail(), CHECKMATE)
                .orElseThrow(AuthenticationFailureException::new);
        final RawPassword rawPassword = new RawPassword(loginRequest.getPassword());
        user.checkPassword(rawPassword);

        return createTokenResponse(user.getId());
    }

    @Transactional
    public TokenResponse loginWithGoogle(final String code) {
        final String googleIdToken = googleClient.getIdToken(code);
        final GoogleProfileResponse profileResponse = googleClient.getProfileResponse(googleIdToken);
        final User user = userRepository.findByEmailAndProvider(profileResponse.getEmail(), GOOGLE)
                .orElseGet(() -> saveGoogleUser(profileResponse));

        return createTokenResponse(user.getId());
    }

    public boolean isMaster(final Long meetingId, final Long loginId) {
        final Participant participant = participantRepository
                .findByMeetingIdAndUserId(meetingId, loginId)
                .orElseThrow(ParticipantNotFoundException::new);
        return participant.getIsMaster();
    }

    public TokenResponse refreshTokens(final String oldToken) {
        final RefreshToken refreshToken = refreshTokenProvider.findRefreshToken(oldToken)
                .orElseThrow(InvalidTokenException::ofInvalid);
        validateRefreshTokenExpiration(refreshToken);
        validateUserExists(refreshToken);

        final TokenResponse tokenResponse = createTokenResponse(refreshToken.getUserId());
        refreshTokenProvider.remove(oldToken);

        return tokenResponse;
    }

    private TokenResponse createTokenResponse(final Long userId) {
        final String accessToken = jwtTokenProvider.createToken(String.valueOf(userId));
        final String refreshToken = refreshTokenProvider.create(userId, serverTimeManager.getDateAndTime());

        return new TokenResponse(accessToken, refreshToken);
    }

    private User saveGoogleUser(final GoogleProfileResponse profileResponse) {
        final User userToSave = new User(profileResponse.getEmail(), profileResponse.getName(), GOOGLE);
        return userRepository.save(userToSave);
    }

    private void validateRefreshTokenExpiration(final RefreshToken refreshToken) {
        if (refreshToken.isExpired(serverTimeManager.getDateAndTime())) {
            removeTokenAndThrowException(refreshToken);
        }
    }

    private void validateUserExists(final RefreshToken refreshToken) {
        if (!userRepository.existsById(refreshToken.getUserId())) {
            removeTokenAndThrowException(refreshToken);
        }
    }

    private void removeTokenAndThrowException(final RefreshToken refreshToken) {
        refreshTokenProvider.remove(refreshToken.getValue());
        throw InvalidTokenException.ofInvalid();
    }

    public void removeRefreshToken(final String refreshToken) {
        refreshTokenProvider.remove(refreshToken);
    }
}
