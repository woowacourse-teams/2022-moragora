package com.woowacourse.moragora;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class FailTest {

    @Test
    void test() {
        Assertions.assertThat(false).isTrue();
    }
}
