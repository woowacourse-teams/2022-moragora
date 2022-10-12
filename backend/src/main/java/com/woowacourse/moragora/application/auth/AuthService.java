package com.woowacourse.moragora.application.auth;

import static com.woowacourse.moragora.domain.user.Provider.CHECKMATE;
import static com.woowacourse.moragora.domain.user.Provider.GOOGLE;

import com.woowacourse.moragora.application.ServerTimeManager;
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
import com.woowacourse.moragora.dto.session.EmailVerificationInfo;
import com.woowacourse.moragora.exception.participant.ParticipantNotFoundException;
import com.woowacourse.moragora.exception.user.AuthenticationFailureException;
import com.woowacourse.moragora.infrastructure.GoogleClient;
import com.woowacourse.moragora.infrastructure.MailSender;
import com.woowacourse.moragora.support.JwtTokenProvider;
import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AuthService {

    private static final int AUTH_CODE_LENGTH = 6;
    private static final int AUTH_CODE_EXPIRE_MINUTE = 5;
    private static final String ATTRIBUTE_NAME_EMAIL_VERIFICATION = "emailVerification";

    private final JwtTokenProvider jwtTokenProvider;
    private final GoogleClient googleClient;
    private final UserRepository userRepository;
    private final ParticipantRepository participantRepository;
    private final MailSender mailSender;
    private final ServerTimeManager serverTimeManager;

    public AuthService(final JwtTokenProvider jwtTokenProvider,
                       final GoogleClient googleClient,
                       final UserRepository userRepository,
                       final ParticipantRepository participantRepository,
                       final MailSender mailSender,
                       final ServerTimeManager serverTimeManager) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.googleClient = googleClient;
        this.userRepository = userRepository;
        this.participantRepository = participantRepository;
        this.mailSender = mailSender;
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
        final String authCode = generateAuthCode();
        mailSender.send(email, authCode);

        final LocalDateTime expiredDateTime = serverTimeManager.plusMinutes(AUTH_CODE_EXPIRE_MINUTE);
        final EmailVerificationInfo emailVerificationInfo = new EmailVerificationInfo(email, authCode, expiredDateTime);
        httpSession.setAttribute(ATTRIBUTE_NAME_EMAIL_VERIFICATION, emailVerificationInfo);
        return new ExpiredTimeResponse(expiredDateTime);
    }

    private String generateAuthCode() {
        final ThreadLocalRandom random = ThreadLocalRandom.current();
        final StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < AUTH_CODE_LENGTH; i++) {
            stringBuilder.append(random.nextInt(10));
        }
        return stringBuilder.toString();
    }
}
