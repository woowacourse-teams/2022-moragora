package com.woowacourse.moragora.support;

import java.util.concurrent.ThreadLocalRandom;
import org.springframework.stereotype.Component;

@Component
public class RandomCodeGenerator {

    private static final int CODE_LENGTH = 6;

    public String generateAuthCode() {
        final ThreadLocalRandom random = ThreadLocalRandom.current();
        final StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < CODE_LENGTH; i++) {
            stringBuilder.append(random.nextInt(10));
        }
        return stringBuilder.toString();
    }
}
