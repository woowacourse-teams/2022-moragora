package com.woowacourse.moragora.service;

import com.woowacourse.moragora.dto.UserRequest;
import com.woowacourse.moragora.entity.User;
import com.woowacourse.moragora.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    public UserService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Long create(final UserRequest userRequest) {
        final User user = userRequest.toEntity();
        final User savedUser = userRepository.save(user);
        return savedUser.getId();
    }
}
