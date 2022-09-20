package com.woowacourse.logging;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

@Slf4j
@Component
public class LoggingFilter extends OncePerRequestFilter {

    private static final String HTTP_REQUEST_FORMAT = "\n### HTTP REQUEST ###\nMethod: {}\nURI: {}\n";
    private static final String REQUEST_AUTHORIZATION_FORMAT = "Authorization: {}\n";
    private static final String REQUEST_BODY_FORMAT = "Content-Type: {}\nBody: {}\n";
    private static final String HTTP_RESPONSE_FORMAT = "\n### HTTP RESPONSE ###\nStatusCode: {}";
    private static final String HTTP_RESPONSE_WITH_BODY_FORMAT = HTTP_RESPONSE_FORMAT + "\nBody: {}";
    private static final String QUERY_COUNTER_FORMAT = "\n### Request Processed ###\n{} {} [Time: {} ms] [Queries: {}]";
    private static final String QUERY_STRING_PREFIX = "?";
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String AUTHORIZATION_SPLIT_DELIMITER = " ";
    private static final int AUTHORIZATION_CREDENTIALS_INDEX = 1;
    private static final char MASKED_CHARACTER = '*';

    private final QueryCountInspector queryCountInspector;

    public LoggingFilter(final QueryCountInspector queryCountInspector) {
        this.queryCountInspector = queryCountInspector;
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final FilterChain filterChain) throws ServletException, IOException {
        final ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        final ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);
        final long startTime = System.currentTimeMillis();
        MDC.put("traceId", UUID.randomUUID().toString());
        queryCountInspector.startCounter();

        filterChain.doFilter(wrappedRequest, wrappedResponse);

        logProcessedRequest(wrappedRequest, wrappedResponse, System.currentTimeMillis() - startTime);
        wrappedResponse.copyBodyToResponse();
        MDC.clear();
    }

    private void logProcessedRequest(final ContentCachingRequestWrapper request,
                                     final ContentCachingResponseWrapper response,
                                     final long duration) {
        logRequest(request);
        logResponse(response);
        logQueryCount(duration, request.getMethod(), request.getRequestURI());
    }

    private void logQueryCount(final long duration, final String method, final String uri) {
        final Long queryCount = queryCountInspector.getQueryCount();
        Object[] args = new Object[]{method, uri, duration, queryCount};

        if (queryCount >= 10) {
            log.warn(QUERY_COUNTER_FORMAT, args);
            return;
        }

        log.info(QUERY_COUNTER_FORMAT, args);
    }

    private void logRequest(final ContentCachingRequestWrapper request) {
        final String requestBody = new String(request.getContentAsByteArray());

        if (request.getHeader(AUTHORIZATION_HEADER) == null) {
            log.info(HTTP_REQUEST_FORMAT + REQUEST_BODY_FORMAT,
                    getRequestUri(request),
                    request.getMethod(),
                    request.getContentType(),
                    requestBody);
            return;
        }

        log.info(HTTP_REQUEST_FORMAT + REQUEST_AUTHORIZATION_FORMAT + REQUEST_BODY_FORMAT,
                getRequestUri(request),
                request.getMethod(),
                mask(request.getHeader(AUTHORIZATION_HEADER)),
                request.getContentType(),
                requestBody);
    }

    private void logResponse(final ContentCachingResponseWrapper response) {
        final Optional<String> body = getJsonResponseBody(response);

        if (body.isPresent()) {
            log.info(HTTP_RESPONSE_WITH_BODY_FORMAT, response.getStatus(), body.get());
            return;
        }

        log.info(HTTP_RESPONSE_FORMAT, response.getStatus());
    }

    private String getRequestUri(final ContentCachingRequestWrapper request) {
        final String requestURI = request.getRequestURI();
        final String queryString = request.getQueryString();

        if (queryString == null) {
            return requestURI;
        }

        return requestURI + QUERY_STRING_PREFIX + queryString;
    }

    private String mask(final String authorization) {
        final String[] splitValue = authorization.split(AUTHORIZATION_SPLIT_DELIMITER);
        final StringBuilder stringBuilder = new StringBuilder(splitValue[AUTHORIZATION_CREDENTIALS_INDEX]);
        IntStream.range(0, stringBuilder.length())
                .forEach(it -> stringBuilder.setCharAt(it, MASKED_CHARACTER));

        return stringBuilder.toString();
    }

    private Optional<String> getJsonResponseBody(final ContentCachingResponseWrapper response) {
        if (Objects.equals(response.getContentType(), MediaType.APPLICATION_JSON_VALUE)) {
            return Optional.of(new String(response.getContentAsByteArray()));
        }

        return Optional.empty();
    }
}
