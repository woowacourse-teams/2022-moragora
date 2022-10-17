package com.woowacourse.moragora.acceptance;

import static com.woowacourse.moragora.support.fixture.MeetingFixtures.MORAGORA;
import static com.woowacourse.moragora.support.fixture.UserFixtures.BATD;
import static com.woowacourse.moragora.support.fixture.UserFixtures.KUN;
import static com.woowacourse.moragora.support.fixture.UserFixtures.MASTER;
import static com.woowacourse.moragora.support.fixture.UserFixtures.createUsers;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import com.woowacourse.moragora.domain.meeting.Meeting;
import com.woowacourse.moragora.domain.user.User;
import com.woowacourse.moragora.dto.request.user.NicknameRequest;
import com.woowacourse.moragora.dto.request.user.PasswordRequest;
import com.woowacourse.moragora.dto.request.user.UserDeleteRequest;
import com.woowacourse.moragora.dto.request.user.UserRequest;
import io.restassured.response.ValidatableResponse;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("회원 관련 기능")
class UserAcceptanceTest extends AcceptanceTest {

    @DisplayName("이메일, 비밀번호, 닉네임 모든 양식에 맞게 작성하면 회원가입에 성공하고 상태코드 201을 반환받는다.")
    @Test
    void signUp() {
        // given
        final String email = "kun@naver.com";
        final String sessionId = verifyEmailAndGetSessionId(email);
        final UserRequest userRequest = new UserRequest(email, "1234smart!", "kun");

        // when
        final ValidatableResponse response = postWithSession("/users", userRequest, sessionId);

        // then
        response.statusCode(HttpStatus.CREATED.value())
                .header("Location", notNullValue());
    }

    @DisplayName("키워드를 입력하면 그 키워드가 포함된 유저 목록과 상태코드 200을 반환한다.")
    @Test
    void search() {
        // given
        final String keyword = "email";
        final List<User> users = createUsers();
        final List<Long> ids = saveUsers(users);

        final List<Integer> userIds = ids.stream()
                .map(Long::intValue)
                .collect(Collectors.toList());

        final List<String> nicknames = users.stream()
                .map(User::getNickname)
                .collect(Collectors.toList());

        final List<String> emails = users.stream()
                .map(User::getEmail)
                .collect(Collectors.toList());

        final List<String> providers = users.stream()
                .map(user -> user.getProvider().name().toLowerCase())
                .collect(Collectors.toList());

        // when
        final ValidatableResponse response = get("/users?keyword=" + keyword);

        // then
        response.statusCode(HttpStatus.OK.value())
                .body("users.id", equalTo(userIds))
                .body("users.nickname", equalTo(nicknames))
                .body("users.email", equalTo(emails))
                .body("users.authProvider", equalTo(providers));
    }

    @DisplayName("로그인 한 상태에서 자신의 회원정보를 요청하면 회원정보와 상태코드 200을 반환받는다.")
    @Test
    void findMe() {
        // given
        final User user = MASTER.create();
        final Long id = signUp(user);

        // when
        ValidatableResponse response = get("/users/me", login(user));

        // then
        response.statusCode(HttpStatus.OK.value())
                .body("id", equalTo(id.intValue()))
                .body("email", equalTo(user.getEmail()))
                .body("nickname", equalTo(user.getNickname()))
                .body("authProvider", equalTo(user.getProvider().name().toLowerCase()));
    }

    @DisplayName("로그인 한 상태에서 닉네임 수정을 요청하면 닉네임을 수정한 후 상태코드 204을 반환한다.")
    @Test
    void changeMyNickname() {
        // given
        final String token = signUpAndGetToken(BATD.create());
        final NicknameRequest request = new NicknameRequest("반듯");

        // when
        final ValidatableResponse response = put("/users/me/nickname", request, token);

        // then
        response.statusCode(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("로그인 한 상태에서 비밀번호 수정을 요청하면 비밀번호를 수정한 후 상태코드 204을 반환한다.")
    @Test
    void changeMyPassword() {
        // given
        final String token = signUpAndGetToken(BATD.create());
        final PasswordRequest request = new PasswordRequest("1234asdf!", "new1234!");

        // when
        final ValidatableResponse response = put("/users/me/password", request, token);

        // then
        response.statusCode(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("로그인 한 상태에서 기존 비밀번호를 틀리게 입력하고 비밀번호 수정을 요청하면 상태코드 400을 반환한다.")
    @Test
    void changeMyPassword_throwsException_ifWrongOldPassword() {
        // given
        final String token = signUpAndGetToken(BATD.create());
        final PasswordRequest request = new PasswordRequest("1234wrong!", "new1234!");

        // when
        final ValidatableResponse response = put("/users/me/password", request, token);

        // then
        response.statusCode(HttpStatus.BAD_REQUEST.value())
                .body("message", equalTo("비밀번호가 올바르지 않습니다."));
    }

    @DisplayName("로그인 한 상태에서 기존 비밀번호와 새 비밀번호를 동일하게 입력하고 비밀번호 수정을 요청하면 상태코드 400을 반환한다.")
    @Test
    void changeMyPassword_throwsException_ifSamePassword() {
        // given
        final String token = signUpAndGetToken(BATD.create());
        final PasswordRequest request = new PasswordRequest("1234asdf!", "1234asdf!");

        // when
        final ValidatableResponse response = put("/users/me/password", request, token);

        // then
        response.statusCode(HttpStatus.BAD_REQUEST.value())
                .body("message", equalTo("새로운 비밀번호가 기존의 비밀번호와 일치합니다."));
    }

    @DisplayName("로그인 한 상태에서 회원 탈퇴를 요청하면 상태코드 204를 반환받는다.")
    @Test
    void deleteMe() {
        // given
        final User user = KUN.create();
        final String token = signUpAndGetToken(user);
        final UserDeleteRequest request = new UserDeleteRequest("1234asdf!");

        // when
        ValidatableResponse response = delete("/users/me", request, token);

        // then
        response.statusCode(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("로그인 한 상태에서 잘못된 비밀번호로 회원 탈퇴를 요청하면 상태코드 400을 반환받는다.")
    @Test
    void deleteMe_throwsException_ifWrongPassword() {
        // given
        final User user = KUN.create();
        final String token = signUpAndGetToken(user);
        final UserDeleteRequest request = new UserDeleteRequest("1234wrong!");

        // when
        ValidatableResponse response = delete("/users/me", request, token);

        // then
        response.statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("마스터인 모임이 있는 상태에서 회원 탈퇴를 요청하면 상태코드 403을 반환받는다.")
    @Test
    void deleteMe_throwsException_ifMaster() {
        // given
        final User master = MASTER.create();
        final String token = signUpAndGetToken(master);

        final List<User> users = createUsers();
        final List<Long> userIds = saveUsers(users);
        final Meeting meeting = MORAGORA.create();
        saveMeeting(token, userIds, meeting);

        final UserDeleteRequest request = new UserDeleteRequest("1234asdf!");

        // when
        ValidatableResponse response = delete("/users/me", request, token);

        // then
        response.statusCode(HttpStatus.FORBIDDEN.value())
                .body("message", equalTo("마스터로 참여중인 모임이 있어 탈퇴할 수 없습니다."));
    }
}
