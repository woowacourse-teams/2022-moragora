package com.woowacourse.moragora.repository.user;

import com.woowacourse.moragora.entity.user.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserSpringJpaRepository extends JpaRepository<User, Long>, UserRepository {
    
    List<User> findByIdIn(final List<Long> ids);

    Optional<User> findByEmail(final String email);

    @Query("select u from User u where u.nickname like %:keyword% or u.email like %:keyword%")
    List<User> findByNicknameContainingOrEmailContaining(final String keyword);
}
