package com.woowacourse.logging;

import org.hibernate.resource.jdbc.spi.StatementInspector;
import org.springframework.stereotype.Component;

@Component
public class QueryCountInspector implements StatementInspector {

    private final ThreadLocal<Long> queryCount = new ThreadLocal<>();

    public void startCounter() {
        queryCount.set(0L);
    }

    public Long getQueryCount() {
        return queryCount.get();
    }

    public void clearCounter() {
        queryCount.remove();
    }

    @Override
    public String inspect(final String sql) {
        increaseCount();
        return sql;
    }

    private void increaseCount() {
        final Long count = queryCount.get();
        if (count != null) {
            queryCount.set(count + 1);
        }
    }
}
