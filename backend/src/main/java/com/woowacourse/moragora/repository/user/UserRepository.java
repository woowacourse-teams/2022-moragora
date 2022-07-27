package com.woowacourse.moragora.repository.user;

import com.woowacourse.moragora.entity.user.User;
import java.util.List;
import java.util.Optional;

public interface UserRepository {

    User save(final User user);

    Optional<User> findById(final Long userId);

    List<User> findByIdIn(final List<Long> ids);

    Optional<User> findByEmail(final String email);

    List<User> findByNicknameContainingOrEmailContaining(final String keyword);
}
