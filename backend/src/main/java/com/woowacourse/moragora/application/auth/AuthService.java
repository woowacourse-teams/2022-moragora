package com.woowacourse.moragora.application.auth;

import static com.woowacourse.moragora.domain.user.Provider.CHECKMATE;
import static com.woowacourse.moragora.domain.user.Provider.GOOGLE;

import com.woowacourse.moragora.application.ServerTimeManager;
import com.woowacourse.moragora.domain.auth.AuthCode;
import com.woowacourse.moragora.domain.auth.RefreshToken;
import com.woowacourse.moragora.domain.participant.Participant;
import com.woowacourse.moragora.domain.participant.ParticipantRepository;
import com.woowacourse.moragora.domain.user.User;
import com.woowacourse.moragora.domain.user.UserRepository;
import com.woowacourse.moragora.domain.user.password.RawPassword;
import com.woowacourse.moragora.dto.request.auth.EmailRequest;
import com.woowacourse.moragora.dto.request.auth.EmailVerifyRequest;
import com.woowacourse.moragora.dto.request.user.LoginRequest;
import com.woowacourse.moragora.dto.response.user.GoogleProfileResponse;
import com.woowacourse.moragora.exception.ClientRuntimeException;
import com.woowacourse.moragora.exception.auth.InvalidTokenException;
import com.woowacourse.moragora.exception.participant.ParticipantNotFoundException;
import com.woowacourse.moragora.exception.user.AuthenticationFailureException;
import com.woowacourse.moragora.exception.user.EmailDuplicatedException;
import com.woowacourse.moragora.infrastructure.GoogleClient;
import com.woowacourse.moragora.presentation.auth.TokenResponse;
import com.woowacourse.moragora.support.AsyncMailSender;
import com.woowacourse.moragora.support.RandomCodeGenerator;
import org.springframework.http.HttpStatus;
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
    private final AsyncMailSender mailSender;
    private final RandomCodeGenerator randomCodeGenerator;

    public AuthService(final JwtTokenProvider jwtTokenProvider,
                       final RefreshTokenProvider refreshTokenProvider,
                       final ServerTimeManager serverTimeManager,
                       final GoogleClient googleClient,
                       final UserRepository userRepository,
                       final ParticipantRepository participantRepository,
                       final AsyncMailSender mailSender,
                       final RandomCodeGenerator randomCodeGenerator) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.refreshTokenProvider = refreshTokenProvider;
        this.serverTimeManager = serverTimeManager;
        this.googleClient = googleClient;
        this.userRepository = userRepository;
        this.participantRepository = participantRepository;
        this.mailSender = mailSender;
        this.randomCodeGenerator = randomCodeGenerator;
    }

    @Transactional
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

    @Transactional
    public TokenResponse refreshTokens(final String oldToken) {
        final RefreshToken refreshToken = refreshTokenProvider.findRefreshToken(oldToken)
                .orElseThrow(InvalidTokenException::new);
        removeRefreshToken(oldToken);
        validateRefreshTokenExpiration(refreshToken);
        validateUserExists(refreshToken);

        return createTokenResponse(refreshToken.getUserId());
    }

    @Transactional
    public void removeRefreshToken(final String refreshToken) {
        refreshTokenProvider.remove(refreshToken);
    }

    public AuthCode sendAuthCode(final EmailRequest emailRequest) {
        final String email = emailRequest.getEmail();
        checkEmailExist(email);

        final String code = randomCodeGenerator.generateAuthCode();
        final AuthCode authCode = new AuthCode(email, code, serverTimeManager.getDateAndTime());
        mailSender.send(authCode.toMailMessage());

        return authCode;
    }

    public String verifyAuthCode(final EmailVerifyRequest request, final AuthCode authCode) {
        checkAuthCode(authCode);
        final String email = request.getEmail();
        final String verifyCode = request.getVerifyCode();

        authCode.verify(email, verifyCode, serverTimeManager.getDateAndTime());
        return email;
    }

    private User saveGoogleUser(final GoogleProfileResponse profileResponse) {
        final User userToSave = new User(profileResponse.getEmail(), profileResponse.getName(), GOOGLE);
        return userRepository.save(userToSave);
    }

    private void validateRefreshTokenExpiration(final RefreshToken refreshToken) {
        if (refreshToken.isExpiredAt(serverTimeManager.getDateAndTime())) {
            throw new InvalidTokenException();
        }
    }

    private void validateUserExists(final RefreshToken refreshToken) {
        if (!userRepository.existsById(refreshToken.getUserId())) {
            throw new InvalidTokenException();
        }
    }

    private TokenResponse createTokenResponse(final Long userId) {
        final String accessToken = jwtTokenProvider.create(String.valueOf(userId));
        final String refreshToken = refreshTokenProvider.create(userId, serverTimeManager.getDateAndTime());

        return new TokenResponse(accessToken, refreshToken);
    }

    private void checkEmailExist(final String email) {
        if (userRepository.existsByEmail(email)) {
            throw new EmailDuplicatedException();
        }
    }

    private void checkAuthCode(final AuthCode authCode) {
        if (authCode == null) {
            throw new ClientRuntimeException("인증 정보가 존재하지 않습니다.", HttpStatus.NOT_FOUND);
        }
    }
}
