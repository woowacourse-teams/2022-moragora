package com.woowacourse.moragora.repository;

import com.woowacourse.moragora.entity.User;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @Transactional
    void findByMeetingId() {
        long 존재하는_ID = 1L;
        final List<User> users = userRepository.findByMeetingId(존재하는_ID);

        users.forEach(System.out::println);
    }
}
