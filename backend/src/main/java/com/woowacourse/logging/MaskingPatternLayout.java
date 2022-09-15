package com.woowacourse.logging;

import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class MaskingPatternLayout extends PatternLayout {

    private static final String MASKED_STRING = "********";

    private Pattern multilinePattern;
    private final List<String> maskPatterns = new ArrayList<>();

    public void addMaskPattern(final String maskPattern) {
        maskPatterns.add(maskPattern);
        multilinePattern = Pattern.compile(String.join("|", maskPatterns), Pattern.CASE_INSENSITIVE);
    }

    @Override
    public String doLayout(final ILoggingEvent event) {
        return maskMessage(super.doLayout(event));
    }

    private String maskMessage(final String message) {
        if (multilinePattern == null) {
            return message;
        }

        final StringBuilder stringBuilder = new StringBuilder(message);
        final Matcher matcher = multilinePattern.matcher(stringBuilder);
        while (matcher.find()) {
            maskIfMatch(stringBuilder, matcher);
        }

        return stringBuilder.toString();
    }

    private void maskIfMatch(final StringBuilder stringBuilder, final Matcher matcher) {
        IntStream.rangeClosed(1, matcher.groupCount())
                .filter(it -> !Objects.isNull(matcher.group(it)))
                .forEach(it -> stringBuilder.replace(matcher.start(it), matcher.end(it), MASKED_STRING));
    }
}
