package com.woowacourse.moragora.controller;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.woowacourse.auth.exception.LoginFailException;
import com.woowacourse.moragora.dto.ErrorResponse;
import com.woowacourse.moragora.exception.AttendanceNotFoundException;
import com.woowacourse.moragora.exception.ClosingTimeExcessException;
import com.woowacourse.moragora.exception.IllegalParticipantException;
import com.woowacourse.moragora.exception.InvalidFormatException;
import com.woowacourse.moragora.exception.MeetingNotFoundException;
import com.woowacourse.moragora.exception.NoKeywordException;
import com.woowacourse.moragora.exception.NoParameterException;
import com.woowacourse.moragora.exception.ParticipantNotFoundException;
import com.woowacourse.moragora.exception.UserNotFoundException;
import com.woowacourse.moragora.exception.meeting.IllegalStartEndDateException;
import java.util.List;
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
        final FieldError mainError = fieldErrors.get(0);

        return new ErrorResponse(mainError.getDefaultMessage());
    }

    @ExceptionHandler(MeetingNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ErrorResponse handleMeetingNotFoundException(final Exception exception) {
        return new ErrorResponse(exception.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ErrorResponse handleUserNotFoundException(final Exception exception) {
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

    @ExceptionHandler(InvalidFormatException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse handleInvalidFormatException(final Exception exception) {
        return new ErrorResponse(exception.getMessage());
    }

    @ExceptionHandler(LoginFailException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse handleLoginFailException(final Exception exception) {
        return new ErrorResponse(exception.getMessage());
    }

    @ExceptionHandler(IllegalParticipantException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse handleIllegalParticipantException(final Exception exception) {
        return new ErrorResponse(exception.getMessage());
    }

    @ExceptionHandler(NoParameterException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse handleNoParameterException(final Exception exception) {
        return new ErrorResponse(exception.getMessage());
    }

    @ExceptionHandler(NoKeywordException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse handleNoKeywordException(final Exception exception) {
        return new ErrorResponse(exception.getMessage());
    }

    @ExceptionHandler(ParticipantNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ErrorResponse handleParticipantNotFoundException(final Exception exception) {
        return new ErrorResponse(exception.getMessage());
    }

    @ExceptionHandler(ClosingTimeExcessException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse handleClosingTimeExcessException(final Exception exception) {
        return new ErrorResponse(exception.getMessage());
    }

    @ExceptionHandler(AttendanceNotFoundException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse handleAttendanceNotFoundException(final Exception exception) {
        return new ErrorResponse(exception.getMessage());
    }
}
