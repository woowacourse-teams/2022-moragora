package com.woowacourse.moragora.domain.auth;

import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface RefreshTokenRepository extends Repository<RefreshToken, String> {

    RefreshToken save(final RefreshToken refreshToken);

    Optional<RefreshToken> findByUuid(final String uuid);

    void deleteByUuid(final String uuid);
}
