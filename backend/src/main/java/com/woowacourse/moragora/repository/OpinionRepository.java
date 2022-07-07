package com.woowacourse.moragora.repository;

import com.woowacourse.moragora.entity.Opinion;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class OpinionRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    public OpinionRepository(final EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public Opinion save(final Opinion opinion) {
        entityManager.persist(opinion);
        return opinion;
    }
}
