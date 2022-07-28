package com.woowacourse.moragora.repository;

import com.woowacourse.moragora.entity.Master;
import org.springframework.data.repository.Repository;

public interface MasterRepository extends Repository<Master, Long> {

    Master save(final Master master);
}
