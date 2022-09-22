package com.woowacourse.moragora.config;

import com.woowacourse.logging.QueryCountInspector;
import org.hibernate.cfg.AvailableSettings;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HibernateConfig {

    private final QueryCountInspector queryCountInspector;

    public HibernateConfig(final QueryCountInspector queryCountInspector) {
        this.queryCountInspector = queryCountInspector;
    }

    @Bean
    public HibernatePropertiesCustomizer configureStatementInspector() {
        return hibernateProperties ->
                hibernateProperties.put(AvailableSettings.STATEMENT_INSPECTOR, queryCountInspector);
    }
}
