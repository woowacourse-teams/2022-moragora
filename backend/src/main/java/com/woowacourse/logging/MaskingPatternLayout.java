package com.woowacourse.logging;

import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class MaskingPatternLayout extends PatternLayout {

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
        final StringBuilder sb = new StringBuilder(message);
        final Matcher matcher = multilinePattern.matcher(sb);
        while (matcher.find()) {
            IntStream.rangeClosed(1, matcher.groupCount()).forEach(group -> {
                if (matcher.group(group) != null) {
                    IntStream.range(matcher.start(group), matcher.end(group))
                            .forEach(it -> sb.setCharAt(it, '*'));
                }
            });
        }

        return sb.toString();
    }
}
