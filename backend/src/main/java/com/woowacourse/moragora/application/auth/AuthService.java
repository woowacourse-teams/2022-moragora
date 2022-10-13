package com.woowacourse.moragora.application.auth;

import static com.woowacourse.moragora.domain.user.Provider.CHECKMATE;
import static com.woowacourse.moragora.domain.user.Provider.GOOGLE;

import com.woowacourse.moragora.application.ServerTimeManager;
import com.woowacourse.moragora.domain.auth.AuthCode;
import com.woowacourse.moragora.domain.participant.Participant;
import com.woowacourse.moragora.domain.participant.ParticipantRepository;
import com.woowacourse.moragora.domain.user.User;
import com.woowacourse.moragora.domain.user.UserRepository;
import com.woowacourse.moragora.domain.user.password.RawPassword;
import com.woowacourse.moragora.dto.request.auth.EmailRequest;
import com.woowacourse.moragora.dto.request.user.LoginRequest;
import com.woowacourse.moragora.dto.response.auth.ExpiredTimeResponse;
import com.woowacourse.moragora.dto.response.user.GoogleProfileResponse;
import com.woowacourse.moragora.dto.response.user.LoginResponse;
import com.woowacourse.moragora.exception.participant.ParticipantNotFoundException;
import com.woowacourse.moragora.exception.user.AuthenticationFailureException;
import com.woowacourse.moragora.exception.user.EmailDuplicatedException;
import com.woowacourse.moragora.infrastructure.GoogleClient;
import com.woowacourse.moragora.support.JwtTokenProvider;
import javax.servlet.http.HttpSession;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AuthService {

    private static final String ATTRIBUTE_NAME_EMAIL_VERIFICATION = "emailVerification";

    private final JwtTokenProvider jwtTokenProvider;
    private final GoogleClient googleClient;
    private final UserRepository userRepository;
    private final ParticipantRepository participantRepository;
    private final JavaMailSender javaMailSender;
    private final ServerTimeManager serverTimeManager;

    public AuthService(final JwtTokenProvider jwtTokenProvider,
                       final GoogleClient googleClient,
                       final UserRepository userRepository,
                       final ParticipantRepository participantRepository,
                       final JavaMailSender javaMailSender,
                       final ServerTimeManager serverTimeManager) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.googleClient = googleClient;
        this.userRepository = userRepository;
        this.participantRepository = participantRepository;
        this.javaMailSender = javaMailSender;
        this.serverTimeManager = serverTimeManager;
    }

    public LoginResponse createToken(final LoginRequest loginRequest) {
        final User user = userRepository.findByEmailAndProvider(loginRequest.getEmail(), CHECKMATE)
                .orElseThrow(AuthenticationFailureException::new);

        final RawPassword rawPassword = new RawPassword(loginRequest.getPassword());
        user.checkPassword(rawPassword);
        final String accessToken = jwtTokenProvider.createToken(String.valueOf(user.getId()));
        return new LoginResponse(accessToken);
    }

    @Transactional
    public LoginResponse loginWithGoogle(final String code) {
        final String googleIdToken = googleClient.getIdToken(code);
        final GoogleProfileResponse profileResponse = googleClient.getProfileResponse(googleIdToken);
        final User user = userRepository.findByEmailAndProvider(profileResponse.getEmail(), GOOGLE)
                .orElseGet(() -> saveGoogleUser(profileResponse));
        final String accessToken = jwtTokenProvider.createToken(String.valueOf(user.getId()));

        return new LoginResponse(accessToken);
    }

    public boolean isMaster(final Long meetingId, final Long loginId) {
        final Participant participant = participantRepository
                .findByMeetingIdAndUserId(meetingId, loginId)
                .orElseThrow(ParticipantNotFoundException::new);
        return participant.getIsMaster();
    }

    private User saveGoogleUser(final GoogleProfileResponse profileResponse) {
        final User userToSave = new User(profileResponse.getEmail(), profileResponse.getName(), GOOGLE);
        return userRepository.save(userToSave);
    }

    public ExpiredTimeResponse sendAuthCode(final EmailRequest emailRequest, final HttpSession httpSession) {
        final String email = emailRequest.getEmail();
        checkEmailExist(email);

        final AuthCode authCode = new AuthCode(email, serverTimeManager.getDateAndTime());

        javaMailSender.send(authCode.toMailMessage());

        httpSession.setAttribute(ATTRIBUTE_NAME_EMAIL_VERIFICATION, authCode);
        return new ExpiredTimeResponse(authCode.getExpiredTime());
    }

    private void checkEmailExist(final String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new EmailDuplicatedException();
        }
    }
}
