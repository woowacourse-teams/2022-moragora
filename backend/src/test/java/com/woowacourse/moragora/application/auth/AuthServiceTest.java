package com.woowacourse.moragora.application.auth;

import static com.woowacourse.moragora.support.fixture.MeetingFixtures.MORAGORA;
import static com.woowacourse.moragora.support.fixture.UserFixtures.KUN;
import static com.woowacourse.moragora.support.fixture.UserFixtures.PHILLZ;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import com.woowacourse.moragora.application.ServerTimeManager;
import com.woowacourse.moragora.application.UserService;
import com.woowacourse.moragora.domain.meeting.Meeting;
import com.woowacourse.moragora.domain.user.User;
import com.woowacourse.moragora.dto.request.user.LoginRequest;
import com.woowacourse.moragora.dto.request.user.UserDeleteRequest;
import com.woowacourse.moragora.dto.request.user.UserRequest;
import com.woowacourse.moragora.dto.response.user.GoogleProfileResponse;
import com.woowacourse.moragora.dto.response.user.UserResponse;
import com.woowacourse.moragora.exception.auth.InvalidTokenException;
import com.woowacourse.moragora.exception.user.AuthenticationFailureException;
import com.woowacourse.moragora.infrastructure.GoogleClient;
import com.woowacourse.moragora.presentation.auth.TokenResponse;
import com.woowacourse.moragora.support.DataSupport;
import com.woowacourse.moragora.support.DatabaseCleanUp;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

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

    @Autowired
    private ServerTimeManager serverTimeManager;

    @MockBean
    private GoogleClient googleClient;

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
        final TokenResponse tokenResponse = authService.login(loginRequest);

        // then
        assertAll(
                () -> assertThat(tokenResponse.getAccessToken()).isNotNull(),
                () -> assertThat(tokenResponse.getRefreshToken()).isNotNull()
        );
    }

    @DisplayName("존재하지 않는 이메일로 로그인 시도시 예외가 발생한다.")
    @Test
    void createToken_throwsException_ifEmailIsNotExist() {
        // given
        final String email = "kun@email.com";
        final String password = "qwerasdf123!";

        final LoginRequest loginRequest = new LoginRequest(email, password);

        // when, then
        assertThatThrownBy(() -> authService.login(loginRequest))
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
        assertThatThrownBy(() -> authService.login(loginRequest))
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
        final TokenResponse tokenResponse = authService.loginWithGoogle("codecode");
        final String payload = jwtTokenProvider.getPayload(tokenResponse.getAccessToken());
        final UserResponse response = userService.findById(Long.parseLong(payload));

        // then
        assertThat(response).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expectedResponse);
    }

    @DisplayName("Refresh token이 유효할 경우 새로운 access token과 refresh token을 발급한다.")
    @ParameterizedTest
    @ValueSource(longs = {0, 4, 7})
    void refreshTokens(final long days) {

        // given
        final String email = "kun@email.com";
        final String password = "qwerasdf123!";
        final UserRequest userRequest = new UserRequest(email, password, "kun");
        userService.create(userRequest);
        final LoginRequest loginRequest = new LoginRequest(email, password);
        final TokenResponse tokenResponse = authService.login(loginRequest);

        // when
        serverTimeManager.refresh(LocalDateTime.now().plusDays(days));
        final TokenResponse result = authService.refreshTokens(tokenResponse.getRefreshToken());

        // then
        assertAll(
                () -> assertThat(result.getAccessToken()).isNotNull(),
                () -> assertThat(result.getRefreshToken()).isNotNull(),
                () -> assertThat(result.getRefreshToken()).isNotEqualTo(tokenResponse.getRefreshToken())
        );
    }

    @DisplayName("존재하지 않는 refresh token으로 재발급을 요청할 경우 예외가 발생한다.")
    @Test
    void refreshTokens_ifInvalid_throwsException() {
        // given
        final String invalidToken = "invalid_refresh_token";

        // when, then
        assertThatThrownBy(() -> authService.refreshTokens(invalidToken))
                .isInstanceOf(InvalidTokenException.class);
    }

    @DisplayName("Refresh token이 만료된 경우, (즉 8일이 경과되면) 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(longs = {8, 9, 10})
    void refreshTokens_ifExpired_throwsException(final long days) {
        // given
        final String email = "kun@email.com";
        final String password = "qwerasdf123!";
        final UserRequest userRequest = new UserRequest(email, password, "kun");
        userService.create(userRequest);

        final LoginRequest loginRequest = new LoginRequest(email, password);
        final TokenResponse tokenResponse = authService.login(loginRequest);

        // when, then
        serverTimeManager.refresh(LocalDateTime.now().plusDays(days));
        assertThatThrownBy(() -> authService.refreshTokens(tokenResponse.getRefreshToken()))
                .isInstanceOf(InvalidTokenException.class);
    }

    @DisplayName("Refresh token에 해당하는 유저가 존재하지 않는 경우 예외가 발생한다.")
    @Test
    void refreshTokens_ifUserNotExists_throwsException() {
        // given
        final String email = "kun@email.com";
        final String password = "qwerasdf123!";
        final UserRequest userRequest = new UserRequest(email, password, "kun");
        final Long userId = userService.create(userRequest);

        final LoginRequest loginRequest = new LoginRequest(email, password);
        final TokenResponse tokenResponse = authService.login(loginRequest);

        // when, then
        userService.delete(new UserDeleteRequest(password), userId);
        serverTimeManager.refresh(LocalDateTime.now());
        assertThatThrownBy(() -> authService.refreshTokens(tokenResponse.getRefreshToken()))
                .isInstanceOf(InvalidTokenException.class);
    }
}
