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

    private static final String HTTP_REQUEST_FORMAT = "\n### HTTP REQUEST ###\n" +
            "Method: {}\n" +
            "URI: {}\n";
    private static final String REQUEST_AUTHORIZATION_FORMAT = "Authorization: {}\n";
    private static final String REQUEST_BODY_FORMAT = "Content-Type: {}\nBody: {}\n";
    private static final String HTTP_RESPONSE_FORMAT = "\n### HTTP RESPONSE ###\n" +
            "StatusCode: {}";
    private static final String HTTP_RESPONSE_WITH_BODY_FORMAT = HTTP_RESPONSE_FORMAT + "\nBody: {}";
    private static final String QUERY_STRING_PREFIX = "?";
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String AUTHORIZATION_SPLIT_DELIMITER = " ";
    private static final int AUTHORIZATION_CREDENTIALS_INDEX = 1;
    private static final char MASKED_CHARACTER = '*';

    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final FilterChain filterChain) throws ServletException, IOException {
        final ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        final ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

        MDC.put("traceId", UUID.randomUUID().toString());
        filterChain.doFilter(wrappedRequest, wrappedResponse);

        logRequest(wrappedRequest);
        logResponse(wrappedResponse);
        wrappedResponse.copyBodyToResponse();
        MDC.clear();
    }

    private void logRequest(final ContentCachingRequestWrapper request) {
        final String requestBody = new String(request.getContentAsByteArray());

        if (Objects.isNull(request.getHeader(AUTHORIZATION_HEADER))) {
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

        if (Objects.isNull(queryString)) {
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
