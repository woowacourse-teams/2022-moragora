package com.woowacourse.moragora.repository;

import com.woowacourse.moragora.entity.Discussion;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


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

    public List<Discussion> findAll() {
        return entityManager.createQuery("select d from Discussion d", Discussion.class).getResultList();
    }
}
