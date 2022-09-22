package com.woowacourse.logging;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class QueryCountInspectorTest {

    @DisplayName("객체가 생성되었는지 확인한다.")
    @Test
    void startCounter() {
        // given
        final QueryCountInspector queryCountInspector = new QueryCountInspector();

        // when
        queryCountInspector.startCounter();

        // then
        assertThat(queryCountInspector.getQueryCount()).isNotNull();

    }

    @DisplayName("초기 QueryCount를 반환한다.")
    @Test
    void getQueryCount() {
        // given
        final QueryCountInspector queryCountInspector = new QueryCountInspector();
        queryCountInspector.startCounter();

        // when, then
        assertThat(queryCountInspector.getQueryCount()).isZero();
    }

    @DisplayName("Query Counter를 제거한다.")
    @Test
    void clearCounter() {
        // given
        final QueryCountInspector queryCountInspector = new QueryCountInspector();
        queryCountInspector.startCounter();

        // when
        queryCountInspector.clearCounter();

        // then
        assertThat(queryCountInspector.getQueryCount()).isNull();
    }

    @DisplayName("Query Counter가 생성되어 있고, 쿼리가 실행되었다면 counter를 1 만큼 증가시키고 sql을 반환한다.")
    @Test
    void inspect() {
        // given
        final QueryCountInspector queryCountInspector = new QueryCountInspector();
        queryCountInspector.startCounter();

        // when
        final String expected = queryCountInspector.inspect("sql");

        // then
        assertAll(
                () -> assertThat(expected).isEqualTo("sql"),
                () -> assertThat(queryCountInspector.getQueryCount()).isOne()
        );
    }

    @DisplayName("Query Counter가 생성되어 있지 않고, 쿼리가 실행되었다면 counter가 null이고 sql만 반환한다.")
    @Test
    void inspect_doesNotIncreaseCount_onlyReturnsSql() {
        // given
        final QueryCountInspector queryCountInspector = new QueryCountInspector();

        // when
        final String expected = queryCountInspector.inspect("sql");

        // then
        assertAll(
                () -> assertThat(expected).isEqualTo("sql"),
                () -> assertThat(queryCountInspector.getQueryCount()).isNull()
        );
    }
}
