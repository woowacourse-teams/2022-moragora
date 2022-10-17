package com.woowacourse.moragora.domain.user;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends Repository<User, Long> {

    User save(final User user);

    Optional<User> findById(final Long id);

    List<User> findByIdIn(final List<Long> ids);

    boolean existsByEmail(final String email);

    Optional<User> findByEmailAndProvider(final String email, final Provider provider);

    @Query("select u from User u where u.nickname like %:keyword% or u.email like %:keyword%")
    List<User> findByNicknameOrEmailLike(@Param("keyword") final String keyword);

    void delete(final User user);
}
