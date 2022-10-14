package com.woowacourse.moragora.application.auth;

import static com.woowacourse.moragora.support.fixture.MeetingFixtures.MORAGORA;
import static com.woowacourse.moragora.support.fixture.UserFixtures.KUN;
import static com.woowacourse.moragora.support.fixture.UserFixtures.PHILLZ;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.woowacourse.moragora.application.ServerTimeManager;
import com.woowacourse.moragora.application.UserService;
import com.woowacourse.moragora.domain.auth.AuthCode;
import com.woowacourse.moragora.domain.meeting.Meeting;
import com.woowacourse.moragora.domain.user.User;
import com.woowacourse.moragora.dto.request.auth.EmailRequest;
import com.woowacourse.moragora.dto.request.auth.EmailVerifyRequest;
import com.woowacourse.moragora.dto.request.user.LoginRequest;
import com.woowacourse.moragora.dto.request.user.UserRequest;
import com.woowacourse.moragora.dto.response.auth.ExpiredTimeResponse;
import com.woowacourse.moragora.dto.response.user.GoogleProfileResponse;
import com.woowacourse.moragora.dto.response.user.LoginResponse;
import com.woowacourse.moragora.dto.response.user.UserResponse;
import com.woowacourse.moragora.exception.ClientRuntimeException;
import com.woowacourse.moragora.exception.user.AuthenticationFailureException;
import com.woowacourse.moragora.exception.user.EmailDuplicatedException;
import com.woowacourse.moragora.infrastructure.GoogleClient;
import com.woowacourse.moragora.support.DataSupport;
import com.woowacourse.moragora.support.DatabaseCleanUp;
import com.woowacourse.moragora.support.JwtTokenProvider;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import javax.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@SpringBootTest
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @Autowired
    private DataSupport dataSupport;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private GoogleClient googleClient;

    @MockBean
    private JavaMailSenderImpl javaMailSender;

    @Autowired
    private ServerTimeManager serverTimeManager;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @BeforeEach
    void setUp() {
        databaseCleanUp.execute();
    }

    @DisplayName("로그인 정보를 받아 토큰을 생성한다.")
    @Test
    void createToken() {
        // given
        final String email = "kun@email.com";
        final String password = "qwerasdf123!";
        final UserRequest userRequest = new UserRequest(email, password, "kun");
        userService.create(userRequest);

        final LoginRequest loginRequest = new LoginRequest(email, password);

        // when
        final LoginResponse response = authService.createToken(loginRequest);

        // then
        assertThat(response.getAccessToken()).isNotNull();
    }

    @DisplayName("존재하지 않는 이메일로 로그인 시도시 예외가 발생한다.")
    @Test
    void createToken_throwsException_ifEmailIsNotExist() {
        // given
        final String email = "kun@email.com";
        final String password = "qwerasdf123!";

        final LoginRequest loginRequest = new LoginRequest(email, password);

        // when, then
        assertThatThrownBy(() -> authService.createToken(loginRequest))
                .isInstanceOf(AuthenticationFailureException.class);
    }

    @DisplayName("잘못된 비밀번호로 로그인 시도시 예외가 발생한다.")
    @Test
    void createToken_throwsException_ifPasswordIsWrong() {
        // given
        final String email = "kun@email.com";
        final String password = "qwerasdf123!";
        final UserRequest userRequest = new UserRequest(email, password, "kun");
        userService.create(userRequest);

        final LoginRequest loginRequest = new LoginRequest(email, "smart1234!");

        // when, then
        assertThatThrownBy(() -> authService.createToken(loginRequest))
                .isInstanceOf(AuthenticationFailureException.class);
    }

    @DisplayName("해당 유저가 해당 미팅의 마스터인지 체크한다.")
    @Test
    void isMaster() {
        // given
        final User user1 = dataSupport.saveUser(KUN.create());
        final User user2 = dataSupport.saveUser(PHILLZ.create());

        final Meeting meeting = dataSupport.saveMeeting(MORAGORA.create());

        dataSupport.saveParticipant(user1, meeting, true);
        dataSupport.saveParticipant(user2, meeting, false);

        // when, then
        assertThat(authService.isMaster(meeting.getId(), user1.getId())).isTrue();
    }

    @DisplayName("해당 유저가 해당 미팅의 마스터인지 체크한다(아닌 경우)")
    @Test
    void isMaster_Not() {
        // given
        final User user1 = dataSupport.saveUser(KUN.create());
        final User user2 = dataSupport.saveUser(PHILLZ.create());

        final Meeting meeting = dataSupport.saveMeeting(MORAGORA.create());

        dataSupport.saveParticipant(user1, meeting, true);
        dataSupport.saveParticipant(user2, meeting, false);

        // when, then
        assertThat(authService.isMaster(meeting.getId(), user2.getId())).isFalse();
    }

    @DisplayName("구글 OAuth 로그인 시 회원가입이 이루어졌는지 확인한다.")
    @Test
    void loginWithGoogle() {
        // given
        given(googleClient.getIdToken(anyString()))
                .willReturn("something");
        given(googleClient.getProfileResponse(anyString()))
                .willReturn(new GoogleProfileResponse("sunny@gmail.com", "썬"));
        final UserResponse expectedResponse = new UserResponse(null, "sunny@gmail.com", "썬", "google");

        // when
        final LoginResponse loginResponse = authService.loginWithGoogle("codecode");
        final String payload = jwtTokenProvider.getPayload(loginResponse.getAccessToken());
        final UserResponse response = userService.findById(Long.parseLong(payload));

        // then
        assertThat(response).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expectedResponse);
    }

    @DisplayName("인증 코드를 메일로 전송하고 세션에 이메일 인증관련 정보들을 저장한다.")
    @Test
    void sendAuthCode() {
        // given
        final String email = "ghd700@daum.net";
        final HttpSession httpSession = mock(HttpSession.class);
        final String attributeName = "emailVerification";
        final EmailRequest request = new EmailRequest(email);
        final LocalDateTime dateTime = LocalDateTime.of(2022, 10, 1, 1, 1);
        final long expected = Timestamp.valueOf(dateTime.plusMinutes(5)).getTime();
        serverTimeManager.refresh(dateTime);

        // when
        final ExpiredTimeResponse expiredTimeResponse = authService.sendAuthCode(request, httpSession);

        // then
        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));
        verify(httpSession, times(1)).setAttribute(eq(attributeName), any(AuthCode.class));
        assertThat(expiredTimeResponse.getExpiredTime()).isEqualTo(expected);
    }

    @DisplayName("이미 존재하는 메일 주소로 인증 코드 전송을 요청하면 예외가 발생한다.")
    @Test
    void sendAuthCode_throwsException_ifEmailDuplicated() {
        // given
        final User user1 = dataSupport.saveUser(KUN.create());
        final String email = user1.getEmail();
        final HttpSession httpSession = mock(HttpSession.class);
        final EmailRequest request = new EmailRequest(email);

        // when, then
        assertThatThrownBy(() -> authService.sendAuthCode(request, httpSession))
                .isInstanceOf(EmailDuplicatedException.class);
    }

    @DisplayName("인증 번호의 유효성을 검증하고 인증된 이메일을 세션에 저장한다.")
    @Test
    void verifyAuthCode() {
        // given
        final String email = "ghd700@daum.net";
        final String code = "000000";
        final LocalDateTime dateTime = LocalDateTime.of(2022, 10, 1, 1, 1);
        final AuthCode authCode = new AuthCode(email, code, dateTime);

        final HttpSession httpSession = mock(HttpSession.class);
        final EmailVerifyRequest request = new EmailVerifyRequest(email, code);

        serverTimeManager.refresh(dateTime);
        when(httpSession.getAttribute("emailVerification"))
                .thenReturn(authCode);

        // when
        authService.verifyAuthCode(request, httpSession);

        // then
        verify(httpSession, times(1)).setAttribute("verifiedEmail", email);
    }

    @DisplayName("세션에 인증 정보가 없는 상태로 인증번호의 유효성을 검증하면 예외가 발생한다.")
    @Test
    void verifyAuthCode_throwsException_ifNothingInSession() {
        // given
        final String email = "ghd700@daum.net";
        final HttpSession httpSession = mock(HttpSession.class);
        final EmailVerifyRequest request = new EmailVerifyRequest(email, "000000");

        when(httpSession.getAttribute("emailVerification"))
                .thenReturn(null);

        // when, then
        assertThatThrownBy(() -> authService.verifyAuthCode(request, httpSession))
                .isInstanceOf(ClientRuntimeException.class)
                .hasMessage("인증 정보가 존재하지 않습니다.");
    }

    @DisplayName("잘못된 이메일로 인증번호의 유효성을 검증하면 예외가 발생한다.")
    @Test
    void verifyAuthCode_throwsException_ifWrongEmail() {
        // given
        final String email = "ghd700@daum.net";
        final String code = "000000";
        final LocalDateTime dateTime = LocalDateTime.of(2022, 10, 1, 1, 1);
        final AuthCode authCode = new AuthCode(email, code, dateTime);

        final HttpSession httpSession = mock(HttpSession.class);
        final EmailVerifyRequest request = new EmailVerifyRequest("wrong@daum.net", code);

        serverTimeManager.refresh(dateTime);
        when(httpSession.getAttribute("emailVerification"))
                .thenReturn(authCode);

        // when, then
        assertThatThrownBy(() -> authService.verifyAuthCode(request, httpSession))
                .isInstanceOf(ClientRuntimeException.class)
                .hasMessage("인증을 요청하지 않은 이메일입니다.");
    }

    @DisplayName("잘못된 인증번호로 인증번호의 유효성을 검증하면 예외가 발생한다.")
    @Test
    void verifyAuthCode_throwsException_ifWrongCode() {
        // given
        final String email = "ghd700@daum.net";
        final LocalDateTime dateTime = LocalDateTime.of(2022, 10, 1, 1, 1);
        final AuthCode authCode = new AuthCode(email, "000000", dateTime);

        final HttpSession httpSession = mock(HttpSession.class);
        final EmailVerifyRequest request = new EmailVerifyRequest(email, "wrong");

        serverTimeManager.refresh(dateTime);
        when(httpSession.getAttribute("emailVerification"))
                .thenReturn(authCode);

        // when, then
        assertThatThrownBy(() -> authService.verifyAuthCode(request, httpSession))
                .isInstanceOf(ClientRuntimeException.class)
                .hasMessage("인증코드가 올바르지 않습니다.");
    }

    @DisplayName("만료된 인증번호로 인증번호의 유효성을 검증하면 예외가 발생한다.")
    @Test
    void verifyAuthCode_throwsException_ifExpiredCode() {
        // given
        final String email = "ghd700@daum.net";
        final String code = "000000";
        final LocalDateTime dateTime = LocalDateTime.of(2022, 10, 1, 1, 1);
        final AuthCode authCode = new AuthCode(email, code, dateTime);

        final HttpSession httpSession = mock(HttpSession.class);
        final EmailVerifyRequest request = new EmailVerifyRequest(email, code);

        serverTimeManager.refresh(dateTime.plusMinutes(6));
        when(httpSession.getAttribute("emailVerification"))
                .thenReturn(authCode);

        // when, then
        assertThatThrownBy(() -> authService.verifyAuthCode(request, httpSession))
                .isInstanceOf(ClientRuntimeException.class)
                .hasMessage("인증코드가 만료되었습니다.");
    }
}
