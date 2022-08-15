package com.woowacourse.moragora.acceptance;

import static com.woowacourse.moragora.support.MeetingFixtures.MORAGORA;
import static com.woowacourse.moragora.support.UserFixtures.KUN;
import static com.woowacourse.moragora.support.UserFixtures.MASTER;
import static com.woowacourse.moragora.support.UserFixtures.createUsers;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import com.woowacourse.moragora.dto.UserRequest;
import com.woowacourse.moragora.dto.WithdrawalRequest;
import com.woowacourse.moragora.entity.Meeting;
import com.woowacourse.moragora.entity.user.User;
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
        final UserRequest userRequest = new UserRequest("kun@naver.com", "1234smart!", "kun");

        // when
        final ValidatableResponse response = post("/users", userRequest);

        // then
        response.statusCode(HttpStatus.CREATED.value())
                .header("Location", notNullValue());
    }

    @DisplayName("가입되지 않은 이메일에 대해 중복 확인을 요청하면 false와 상태코드 200을 반환받는다.")
    @Test
    void checkEmail_ifExists() {
        // given
        final String email = "someUniqueEmail@unique.email";

        // when
        final ValidatableResponse response = get("/users/check-email?email=" + email);

        // then
        response.statusCode(HttpStatus.OK.value())
                .body("isExist", equalTo(false));
    }

    @DisplayName("존재하는 이메일에 대해 중복 확인을 요청하면 true와 상태코드 200을 반환받는다.")
    @Test
    void checkEmail_IfNotExist() {
        // given
        final String email = "kun@naver.com";
        final UserRequest userRequest = new UserRequest(email, "1234smart!", "kun");
        post("/users", userRequest);

        // when
        final ValidatableResponse response = get("/users/check-email?email=" + email);

        // then
        response.statusCode(HttpStatus.OK.value())
                .body("isExist", equalTo(true));
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

        // when
        final ValidatableResponse response = get("/users?keyword=" + keyword);

        // then
        response.statusCode(HttpStatus.OK.value())
                .body("users.id", equalTo(userIds))
                .body("users.nickname", equalTo(nicknames))
                .body("users.email", equalTo(emails));
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
                .body("nickname", equalTo(user.getNickname()));
    }

    @DisplayName("로그인 한 상태에서 회원 탈퇴를 요청하면 상태코드 204를 반환받는다.")
    @Test
    void deleteMe() {
        // given
        final User user = KUN.create();
        final String token = signUpAndGetToken(user);
        final WithdrawalRequest request = new WithdrawalRequest("1234asdf!");

        // when
        ValidatableResponse response = delete("/users/me", request, token);

        // then
        response.statusCode(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("로그인 한 상태에서 잘못된 비밀번호로 회원 탈퇴를 요청하면 상태코드 400을 반환받는다.")
    @Test
    void deleteMe_ifWrongPassword() {
        // given
        final User user = KUN.create();
        final String token = signUpAndGetToken(user);
        final WithdrawalRequest request = new WithdrawalRequest("1234wrong!");

        // when
        ValidatableResponse response = delete("/users/me", request, token);

        // then
        response.statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("마스터인 모임이 있는 상태에서 회원 탈퇴를 요청하면 상태코드 403을 반환받는다.")
    @Test
    void deleteMe_ifMaster() {
        // given
        final User master = MASTER.create();
        final String token = signUpAndGetToken(master);

        final List<User> users = createUsers();
        final List<Long> userIds = saveUsers(users);
        final Meeting meeting = MORAGORA.create();
        saveMeeting(token, userIds, meeting);

        final WithdrawalRequest request = new WithdrawalRequest("1234asdf!");

        // when
        ValidatableResponse response = delete("/users/me", request, token);

        // then
        response.statusCode(HttpStatus.FORBIDDEN.value())
                .body("message", equalTo("마스터로 참여중인 모임이 있어 탈퇴할 수 없습니다."));
    }
}
