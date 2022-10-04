package com.woowacourse.moragora.application.auth;

import static com.woowacourse.moragora.domain.user.Provider.CHECKMATE;
import static com.woowacourse.moragora.domain.user.Provider.GOOGLE;

import com.woowacourse.moragora.domain.participant.Participant;
import com.woowacourse.moragora.domain.participant.ParticipantRepository;
import com.woowacourse.moragora.domain.user.User;
import com.woowacourse.moragora.domain.user.UserRepository;
import com.woowacourse.moragora.domain.user.password.RawPassword;
import com.woowacourse.moragora.dto.request.user.LoginRequest;
import com.woowacourse.moragora.dto.response.user.GoogleProfileResponse;
import com.woowacourse.moragora.dto.response.user.LoginResponse;
import com.woowacourse.moragora.exception.participant.ParticipantNotFoundException;
import com.woowacourse.moragora.exception.user.AuthenticationFailureException;
import com.woowacourse.moragora.infrastructure.GoogleClient;
import com.woowacourse.moragora.infrastructure.MailSender;
import com.woowacourse.moragora.support.JwtTokenProvider;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AuthService {

    private static final int AUTH_CODE_LENGTH = 6;

    private final JwtTokenProvider jwtTokenProvider;
    private final GoogleClient googleClient;
    private final UserRepository userRepository;
    private final ParticipantRepository participantRepository;
    private final MailSender mailSender;

    public AuthService(final JwtTokenProvider jwtTokenProvider,
                       final GoogleClient googleClient,
                       final UserRepository userRepository,
                       final ParticipantRepository participantRepository,
                       final MailSender mailSender) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.googleClient = googleClient;
        this.userRepository = userRepository;
        this.participantRepository = participantRepository;
        this.mailSender = mailSender;
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

    public void sendAuthCode(final String email) {
        final String authCode = generateAuthCode();
        mailSender.send(email, authCode);
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
