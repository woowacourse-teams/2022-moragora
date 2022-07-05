package com.woowacourse.moragora.repository;

import com.woowacourse.moragora.entity.Discussion;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


@Repository
@Transactional(readOnly = true)
public class DiscussionRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    public DiscussionRepository(final EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public Discussion save(final Discussion discussion) {
        entityManager.persist(discussion);
        return discussion;
    }
}
