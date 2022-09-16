package com.woowacourse.moragora.infastructure;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowacourse.moragora.dto.response.user.GoogleProfileResponse;
import com.woowacourse.moragora.dto.response.user.GoogleTokenResponse;
import com.woowacourse.moragora.exception.GoogleOAuthFailureException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
@Getter
public class GoogleClient {

    private static final String AUTHORIZATION_TYPE = "Bearer ";
    private static final String JWT_DELIMITER = "\\.";
    private static final String TOKEN_URL = "https://www.googleapis.com/oauth2/v4/token";
    private static final int PAYLOAD = 1;

    private final RestTemplate restTemplate;
    private final String clientId;
    private final String clientSecret;
    private final String grantType;
    private final String redirectUri;

    public GoogleClient(final RestTemplate restTemplate,
                        @Value("${oauth2.google.client-id}") final String clientId,
                        @Value("${oauth2.google.client-secret}") final String clientSecret,
                        @Value("${oauth2.google.grant-type}") final String grantType,
                        @Value("${oauth2.google.redirect-uri}") final String redirectUri) {
        this.restTemplate = restTemplate;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.grantType = grantType;
        this.redirectUri = redirectUri;
    }

    public String getIdToken(final String code) {
        final HttpHeaders headers = getUrlEncodedHeader();
        final MultiValueMap<String, String> parameters = getGoogleRequestParameters(code);
        final HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(parameters, headers);
        final GoogleTokenResponse tokenResponse = requestGoogleToken(httpEntity);
        return tokenResponse.getIdToken();
    }

    private HttpHeaders getUrlEncodedHeader() {
        final HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        return headers;
    }

    private MultiValueMap<String, String> getGoogleRequestParameters(final String code) {
        final MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("code", code);
        parameters.add("client_id", clientId);
        parameters.add("client_secret", clientSecret);
        parameters.add("grant_type", grantType);
        parameters.add("redirect_uri", redirectUri);

        return parameters;
    }

    private GoogleTokenResponse requestGoogleToken(final HttpEntity<MultiValueMap<String, String>> entity) {
        return restTemplate.exchange(TOKEN_URL, HttpMethod.POST, entity, GoogleTokenResponse.class)
                .getBody();
    }

    public GoogleProfileResponse getProfileResponse(final String idToken) {
        try {
            final ObjectMapper objectMapper = new ObjectMapper();
            final Map<String, String> profile = objectMapper.readValue(getProfileFromToken(idToken), Map.class);
            final String email = profile.get("email");
            final String name = profile.get("name");

            return new GoogleProfileResponse(email, name);
        } catch (JsonProcessingException e) {
            throw new GoogleOAuthFailureException("id 토큰을 읽을 수 없습니다.");
        }
    }

    private String getProfileFromToken(final String token) {
        return decode(getPayload(token));
    }

    private String getPayload(final String token) {
        return token.split(JWT_DELIMITER)[PAYLOAD];
    }

    private String decode(final String payload) {
        return new String(Base64.getUrlDecoder().decode(payload), StandardCharsets.UTF_8);
    }
}
