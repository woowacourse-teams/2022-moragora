package com.woowacourse.moragora.service;

import com.woowacourse.moragora.dto.UserRequest;
import com.woowacourse.moragora.dto.UserResponse2;
import com.woowacourse.moragora.dto.UsersResponse;
import com.woowacourse.moragora.entity.user.EncodedPassword;
import com.woowacourse.moragora.entity.user.User;
import com.woowacourse.moragora.exception.NoKeywordException;
import com.woowacourse.moragora.exception.UserNotFoundException;
import com.woowacourse.moragora.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;
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
        final User user = new User(userRequest.getEmail(), EncodedPassword.fromRawValue(userRequest.getPassword()),
                userRequest.getNickname());
        final User savedUser = userRepository.save(user);
        return savedUser.getId();
    }

    public UsersResponse searchByKeyword(final String keyword) {
        validateKeyword(keyword);
        final List<User> searchedUsers = userRepository.findByNicknameOrEmailContaining(keyword);
        final List<UserResponse2> responses = searchedUsers.stream()
                .map(UserResponse2::from)
                .collect(Collectors.toList());
        return new UsersResponse(responses);
    }

    private void validateKeyword(final String keyword) {
        if (keyword.isEmpty()) {
            throw new NoKeywordException();
        }
    }

    public UserResponse2 findById(final Long id) {
        final User user = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
        return UserResponse2.from(user);
    }
}
