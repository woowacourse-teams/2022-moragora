package com.woowacourse.moragora.controller;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.woowacourse.moragora.dto.ErrorResponse;
import com.woowacourse.moragora.exception.MeetingNotFoundException;
import com.woowacourse.moragora.exception.meeting.IllegalStartEndDateException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse handleInvalidRequest(final BindingResult bindingResult) {
        final List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        final String errorMessage = fieldErrors.stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(" "));

        return new ErrorResponse(errorMessage);
    }

    @ExceptionHandler(MeetingNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ErrorResponse handleMeetingNotFoundException(final Exception exception) {
        return new ErrorResponse(exception.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse handleInvalidRequestFormat() {
        return new ErrorResponse("입력 형식이 올바르지 않습니다.");
    }

    @ExceptionHandler(IllegalStartEndDateException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse handleIllegalStartEndDateException(final Exception exception) {
        return new ErrorResponse(exception.getMessage());
    }
}
