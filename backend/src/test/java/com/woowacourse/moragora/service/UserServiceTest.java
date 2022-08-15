package com.woowacourse.moragora.service;

import static com.woowacourse.moragora.support.MeetingFixtures.MORAGORA;
import static com.woowacourse.moragora.support.UserFixtures.KUN;
import static com.woowacourse.moragora.support.UserFixtures.MASTER;
import static com.woowacourse.moragora.support.UserFixtures.createUsers;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.woowacourse.moragora.dto.EmailCheckResponse;
import com.woowacourse.moragora.dto.UserRequest;
import com.woowacourse.moragora.dto.UserResponse;
import com.woowacourse.moragora.dto.UsersResponse;
import com.woowacourse.moragora.dto.WithdrawalRequest;
import com.woowacourse.moragora.entity.user.User;
import com.woowacourse.moragora.exception.ClientRuntimeException;
import com.woowacourse.moragora.exception.NoParameterException;
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

    @DisplayName("회원의 정보를 삭제한다.")
    @Test
    void delete() {
        // given
        final User user = dataSupport.saveUser(KUN.create());
        final WithdrawalRequest request = new WithdrawalRequest("1234asdf!");

        // when, then
        assertThatNoException().isThrownBy(() -> userService.delete(request, user.getId()));
    }

    @DisplayName("존재하지 않는 회원을 탈퇴시키면 예외가 발생한다.")
    @Test
    void delete_throwsException_ifUserNotFound() {
        // given
        final WithdrawalRequest request = new WithdrawalRequest("1234asdf!");

        // when, then
        assertThatThrownBy(() -> userService.delete(request, 100L))
                .isInstanceOf(UserNotFoundException.class);
    }

    @DisplayName("잘못된 비밀번호로 탈퇴하면 예외가 발생한다.")
    @Test
    void delete_throwsException_ifWrongPassword() {
        // given
        final User user = dataSupport.saveUser(KUN.create());
        final WithdrawalRequest request = new WithdrawalRequest("1234wrong!");

        // when, then
        assertThatThrownBy(() -> userService.delete(request, user.getId()))
                .isInstanceOf(ClientRuntimeException.class)
                .hasMessage("비밀번호가 올바르지 않습니다.");
    }

    @DisplayName("모임의 마스터인 회원이 탈퇴하면 예외가 발생한다.")
    @Test
    void delete_throwsException_ifMaster() {
        // given
        final User master = MASTER.create();
        dataSupport.saveParticipant(master, MORAGORA.create(), true);

        final WithdrawalRequest request = new WithdrawalRequest("1234asdf!");

        // when, then
        assertThatThrownBy(() -> userService.delete(request, master.getId()))
                .isInstanceOf(ClientRuntimeException.class)
                .hasMessage("마스터로 참여중인 모임이 있어 탈퇴할 수 없습니다.");
    }
}
