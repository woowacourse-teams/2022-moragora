package com.woowacourse.moragora.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.woowacourse.moragora.dto.SearchedUsersResponse;
import com.woowacourse.moragora.dto.UserRequest;
import com.woowacourse.moragora.exception.NoKeywordException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    private UserService userService;

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

    @DisplayName("keyword로 회원을 검색한다.")
    @Test
    void searchByKeyword() {
        // given
        final String keyword = "foo";

        // when
        final SearchedUsersResponse response = userService.searchByKeyword(keyword);

        // then
        assertThat(response.getUsers()).hasSize(7);
    }

    @DisplayName("keyword를 입력하지 않고 검색하면 예외가 발생한다.")
    @Test
    void searchByKeyword_throwsException_ifNoKeyword() {
        // given, when, then
        assertThatThrownBy(() -> userService.searchByKeyword(""))
                .isInstanceOf(NoKeywordException.class);
    }
}
