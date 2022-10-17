package com.woowacourse.moragora.presentation;

import static org.springframework.http.HttpHeaders.SET_COOKIE;

import com.woowacourse.moragora.dto.response.ErrorResponse;
import com.woowacourse.moragora.dto.response.TokenErrorResponse;
import com.woowacourse.moragora.exception.ClientRuntimeException;
import com.woowacourse.moragora.exception.InvalidTokenException;
import com.woowacourse.moragora.presentation.auth.RefreshTokenCookieProvider;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ControllerAdvice {

    private final RefreshTokenCookieProvider refreshTokenCookieProvider;

    public ControllerAdvice(final RefreshTokenCookieProvider refreshTokenCookieProvider) {
        this.refreshTokenCookieProvider = refreshTokenCookieProvider;
    }

    @ExceptionHandler(ClientRuntimeException.class)
    public ResponseEntity<ErrorResponse> handleClientRuntimeException(final ClientRuntimeException exception) {
        return ResponseEntity.status(exception.getHttpStatus()).body(new ErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ErrorResponse> handleTokenException(final HttpServletResponse response,
                                                              final InvalidTokenException exception) {
        if (exception.getStatus().equals("invalid")) {
            final ResponseCookie responseCookie = refreshTokenCookieProvider.createInvalidCookie();
            response.addHeader(SET_COOKIE, responseCookie.toString());
        }
        return ResponseEntity.status(exception.getHttpStatus())
                .body(new TokenErrorResponse(exception.getMessage(), exception.getStatus()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleInvalidRequest(final BindingResult bindingResult) {
        final List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        final FieldError mainError = fieldErrors.get(0);

        return ResponseEntity.badRequest().body(new ErrorResponse(mainError.getDefaultMessage()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleInvalidRequestFormat() {
        return ResponseEntity.badRequest().body(new ErrorResponse("입력 형식이 올바르지 않습니다."));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleInternalServerError(final Exception exception) {
        log.error("Internal Server Error\n{}", exception.getMessage(), exception);
        return ResponseEntity.internalServerError().body(new ErrorResponse("서버에 오류가 발생했습니다."));
    }
}
