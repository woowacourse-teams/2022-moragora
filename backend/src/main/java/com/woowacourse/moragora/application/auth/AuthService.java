package com.woowacourse.moragora.application.auth;

import static com.woowacourse.moragora.domain.user.Provider.CHECKMATE;
import static com.woowacourse.moragora.domain.user.Provider.GOOGLE;
import static com.woowacourse.moragora.presentation.SessionAttribute.EMAIL_VERIFICATION;
import static com.woowacourse.moragora.presentation.SessionAttribute.VERIFIED_EMAIL;

import com.woowacourse.moragora.application.ServerTimeManager;
import com.woowacourse.moragora.domain.auth.AuthCode;
import com.woowacourse.moragora.domain.auth.RandomCodeGenerator;
import com.woowacourse.moragora.domain.participant.Participant;
import com.woowacourse.moragora.domain.participant.ParticipantRepository;
import com.woowacourse.moragora.domain.user.User;
import com.woowacourse.moragora.domain.user.UserRepository;
import com.woowacourse.moragora.domain.user.password.RawPassword;
import com.woowacourse.moragora.dto.request.auth.EmailRequest;
import com.woowacourse.moragora.dto.request.auth.EmailVerifyRequest;
import com.woowacourse.moragora.dto.request.user.LoginRequest;
import com.woowacourse.moragora.dto.response.auth.ExpiredTimeResponse;
import com.woowacourse.moragora.dto.response.user.GoogleProfileResponse;
import com.woowacourse.moragora.dto.response.user.LoginResponse;
import com.woowacourse.moragora.exception.ClientRuntimeException;
import com.woowacourse.moragora.exception.participant.ParticipantNotFoundException;
import com.woowacourse.moragora.exception.user.AuthenticationFailureException;
import com.woowacourse.moragora.exception.user.EmailDuplicatedException;
import com.woowacourse.moragora.infrastructure.GoogleClient;
import com.woowacourse.moragora.support.AsyncMailSender;
import com.woowacourse.moragora.support.JwtTokenProvider;
import javax.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final GoogleClient googleClient;
    private final UserRepository userRepository;
    private final ParticipantRepository participantRepository;
    private final AsyncMailSender mailSender;
    private final RandomCodeGenerator randomCodeGenerator;
    private final ServerTimeManager serverTimeManager;

    public AuthService(final JwtTokenProvider jwtTokenProvider,
                       final GoogleClient googleClient,
                       final UserRepository userRepository,
                       final ParticipantRepository participantRepository,
                       final AsyncMailSender mailSender,
                       final RandomCodeGenerator randomCodeGenerator,
                       final ServerTimeManager serverTimeManager) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.googleClient = googleClient;
        this.userRepository = userRepository;
        this.participantRepository = participantRepository;
        this.mailSender = mailSender;
        this.randomCodeGenerator = randomCodeGenerator;
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

    public ExpiredTimeResponse sendAuthCode(final EmailRequest emailRequest, final HttpSession httpSession) {
        final String email = emailRequest.getEmail();
        checkEmailExist(email);

        final String code = randomCodeGenerator.generateAuthCode();
        final AuthCode authCode = new AuthCode(email, code, serverTimeManager.getDateAndTime());
        mailSender.send(authCode.toMailMessage());

        httpSession.setAttribute(EMAIL_VERIFICATION.getName(), authCode);
        return new ExpiredTimeResponse(authCode.getExpiredTime());
    }

    public void verifyAuthCode(final EmailVerifyRequest request, final HttpSession httpSession) {
        final String email = request.getEmail();
        final String verifyCode = request.getVerifyCode();

        final AuthCode authCode = getAuthCodeFromSession(httpSession);
        authCode.verify(email, verifyCode, serverTimeManager.getDateAndTime());

        httpSession.setAttribute(VERIFIED_EMAIL.getName(), email);
    }

    private User saveGoogleUser(final GoogleProfileResponse profileResponse) {
        final User userToSave = new User(profileResponse.getEmail(), profileResponse.getName(), GOOGLE);
        return userRepository.save(userToSave);
    }

    private void checkEmailExist(final String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new EmailDuplicatedException();
        }
    }

    private AuthCode getAuthCodeFromSession(final HttpSession httpSession) {
        final Object authCode = httpSession.getAttribute(EMAIL_VERIFICATION.getName());
        if (authCode == null) {
            throw new ClientRuntimeException("인증 정보가 존재하지 않습니다.", HttpStatus.NOT_FOUND);
        }
        return (AuthCode) authCode;
    }
}
