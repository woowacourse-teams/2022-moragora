package com.woowacourse.moragora.service;

import static com.woowacourse.moragora.support.UserFixtures.BATD;
import static com.woowacourse.moragora.support.UserFixtures.createUsers;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.woowacourse.moragora.dto.EmailCheckResponse;
import com.woowacourse.moragora.dto.NicknameRequest;
import com.woowacourse.moragora.dto.PasswordRequest;
import com.woowacourse.moragora.dto.UserRequest;
import com.woowacourse.moragora.dto.UserResponse;
import com.woowacourse.moragora.dto.UsersResponse;
import com.woowacourse.moragora.entity.user.User;
import com.woowacourse.moragora.exception.ClientRuntimeException;
import com.woowacourse.moragora.exception.InvalidFormatException;
import com.woowacourse.moragora.exception.NoParameterException;
import com.woowacourse.moragora.exception.user.InvalidPasswordException;
import com.woowacourse.moragora.exception.user.UserNotFoundException;
import com.woowacourse.moragora.support.DataSupport;
import com.woowacourse.moragora.support.DatabaseCleanUp;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private DataSupport dataSupport;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @BeforeEach
    void setUp() {
        databaseCleanUp.afterPropertiesSet();
        databaseCleanUp.execute();
    }

    @DisplayName("새로운 회원을 생성한다.")
    @Test
    void create() {
        // given
        final UserRequest userRequest = new UserRequest("kun@naver.com", "1234smart!", "kun");

        // when
        final Long id = userService.create(userRequest);

        // then
        assertThat(id).isNotNull();
    }

    @DisplayName("중복되지 않은 이메일의 중복 여부를 확인한다.")
    @Test
    void isEmailExist_ifNotExists() {
        // given
        final String uniqueEmail = "someUniqueEmail@email.unique";

        // when
        final EmailCheckResponse response = userService.isEmailExist(uniqueEmail);

        // then
        assertThat(response.getIsExist()).isFalse();
    }

    @DisplayName("중복된 이메일의 중복 여부를 확인한다.")
    @Test
    void isEmailExist_ifExists() {
        // given
        final String existingEmail = "kun@naver.com";
        final UserRequest userRequest = new UserRequest(existingEmail, "1234smart!", "kun");
        userService.create(userRequest);

        // when
        final EmailCheckResponse response = userService.isEmailExist(existingEmail);

        // then
        assertThat(response.getIsExist()).isTrue();
    }

    @DisplayName("이메일을 입력하지 않고 중복 여부를 확인하면 예외가 발생한다.")
    @ParameterizedTest
    @EmptySource
    @ValueSource(strings = {" "})
    void isEmailExist_throwsException_ifBlank(final String email) {
        // given, when, then
        assertThatThrownBy(() -> userService.isEmailExist(email))
                .isInstanceOf(NoParameterException.class);
    }

    @DisplayName("keyword로 회원을 검색한다.")
    @Test
    void searchByKeyword() {
        // given
        final String keyword = "email";

        final List<User> users = createUsers();
        for (User user : users) {
            userService.create(new UserRequest(user.getEmail(), "1234asdf!", user.getNickname()));
        }

        // when
        final UsersResponse response = userService.searchByKeyword(keyword);

        // then
        assertThat(response.getUsers()).hasSize(7);
    }

    @DisplayName("keyword를 입력하지 않고 검색하면 예외가 발생한다.")
    @Test
    void searchByKeyword_throwsException_ifNoKeyword() {
        // given, when, then
        assertThatThrownBy(() -> userService.searchByKeyword(""))
                .isInstanceOf(NoParameterException.class);
    }

    @DisplayName("아이디로 회원을 조회한다.")
    @Test
    void findById() {
        // given
        final String email = "kun@naver.com";
        final String nickname = "kun";
        final UserRequest userRequest = new UserRequest(email, "1234smart!", nickname);
        final Long id = userService.create(userRequest);

        final UserResponse expectedResponse = new UserResponse(id, email, nickname);

        // when
        final UserResponse response = userService.findById(id);

        // then
        assertThat(response).usingRecursiveComparison()
                .isEqualTo(expectedResponse);
    }

    @DisplayName("존재하지 않는 아이디로 회원을 조회하면 예외가 발생한다.")
    @Test
    void findById_throwsException_ifIdNotFound() {
        // given, when, then
        assertThatThrownBy(() -> userService.findById(0L))
                .isInstanceOf(UserNotFoundException.class);
    }

    @DisplayName("회원의 닉네임을 변경한다.")
    @Test
    void updateNickname() {
        // given
        final User user = dataSupport.saveUser(BATD.create());
        final NicknameRequest request = new NicknameRequest("반듯");

        // when, then
        assertThatNoException().isThrownBy(() -> userService.updateNickname(request, user.getId()));
    }

    @DisplayName("회원의 닉네임을 형식에 맞지 않게 변경하면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"smart쿤!", "smartboykun12345", "smart kun"})
    void updateNickname_throwsException_ifInvalidNickname(final String nickname) {
        // given
        final User user = dataSupport.saveUser(BATD.create());
        final NicknameRequest request = new NicknameRequest(nickname);

        // when, then
        assertThatThrownBy(() -> userService.updateNickname(request, user.getId()))
                .isInstanceOf(InvalidFormatException.class);
    }

    @DisplayName("존재하지 않는 회원의 닉네임을 변경하면 예외가 발생한다.")
    @Test
    void updateNickname_throwsException_ifUserNotFound() {
        // given
        final NicknameRequest request = new NicknameRequest("반듯");

        // when, then
        assertThatThrownBy(() -> userService.updateNickname(request, 100L))
                .isInstanceOf(UserNotFoundException.class);
    }

    @DisplayName("회원의 비밀번호를 변경한다.")
    @Test
    void updatePassword() {
        // given
        final User user = dataSupport.saveUser(BATD.create());
        final PasswordRequest request = new PasswordRequest("1234asdf!", "new1234!");

        // when, then
        assertThatNoException().isThrownBy(() -> userService.updatePassword(request, user.getId()));
    }

    @DisplayName("회원의 비밀번호를 형식에 맞지 않게 변경하면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"password", "password!", "password1", "12345678!", "password!!123456789012345678901"})
    void updatePassword_throwsException_ifInvalidPassword(final String password) {
        // given
        final User user = dataSupport.saveUser(BATD.create());
        final PasswordRequest request = new PasswordRequest("1234asdf!", password);

        // when, then
        assertThatThrownBy(() -> userService.updatePassword(request, user.getId()))
                .isInstanceOf(InvalidFormatException.class);
    }

    @DisplayName("존재하지 않는 회원의 비밀번호를 변경하면 예외가 발생한다.")
    @Test
    void updatePassword_throwsException_ifUserNotFound() {
        // given
        final PasswordRequest request = new PasswordRequest("1234asdf!", "new1234!");

        // when, then
        assertThatThrownBy(() -> userService.updatePassword(request, 100L))
                .isInstanceOf(UserNotFoundException.class);
    }

    @DisplayName("기존 비밀번호를 틀리게 입력하고 비밀번호를 변경하면 예외가 발생한다.")
    @Test
    void updatePassword__throwsException_ifWrongPassword() {
        // given
        final User user = dataSupport.saveUser(BATD.create());
        final PasswordRequest request = new PasswordRequest("1234wrong!", "new1234!");

        // when, then
        assertThatThrownBy(() -> userService.updatePassword(request, 1L))
                .isInstanceOf(InvalidPasswordException.class);
    }

    @DisplayName("기존 비밀번호와 동일한 비밀번호로 변경하면 예외가 발생한다.")
    @Test
    void updatePassword_throwsException_ifSamePassword() {
        // given
        final User user = dataSupport.saveUser(BATD.create());
        final PasswordRequest request = new PasswordRequest("1234asdf!", "1234asdf!");

        // when, then
        assertThatThrownBy(() -> userService.updatePassword(request, 1L))
                .isInstanceOf(ClientRuntimeException.class)
                .hasMessage("새로운 비밀번호가 기존의 비밀번호와 일치합니다.");
    }
}
