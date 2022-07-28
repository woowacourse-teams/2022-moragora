package com.woowacourse.moragora.repository;

import com.woowacourse.moragora.entity.Master;
import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface MasterRepository extends Repository<Master, Long> {

    Master save(final Master master);

    Optional<Master> findByParticipantId(final Long participantId);
}
