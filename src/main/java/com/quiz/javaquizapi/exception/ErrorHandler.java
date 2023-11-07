package com.quiz.javaquizapi.exception;

import com.google.common.base.CaseFormat;
import com.quiz.javaquizapi.model.http.CommonErrors;
import com.quiz.javaquizapi.model.http.Response;
import com.quiz.javaquizapi.service.response.ResponseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Arrays;
import java.util.stream.Stream;

/**
 * Catches all the API exceptions and handles them properly.
 */
@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class ErrorHandler extends ResponseEntityExceptionHandler {
    /**
     * This constant provides an error code for the case when url parameter is invalid.
     * <p>The property key is - '<strong>api.errorCode.45</strong>'.
     * <p>See also: <strong>src/main/resources/messages.properties</strong>.
     */
    public static final int PARAMETER_ERROR_CODE = 45;
    /**
     * This constant provides an error code for the case when provided enum value is invalid.
     * <p>The property key is - '<strong>api.errorCode.46</strong>'.
     * <p>See also: <strong>src/main/resources/messages.properties</strong>.
     */
    public static final int ENUM_VALUES_ERROR_CODE = 46;
    public static final String INVALID_PARAMETER_TYPE_ERROR = "Parameter '%s' value doesn't match required type";
    private final ResponseService responseService;

    /**
     * Handles all unexpected {@link Exception} and its extensions.
     *
     * @param exception thrown exception.
     * @param request   the current request details.
     * @return {@link Response} wrapped by {@link ResponseEntity}.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleUnexpectedException(Exception exception, WebRequest request) {
        log.error("Unexpected error occurred:", exception);
        return handleExceptionInternal(
                exception,
                responseService.buildError(null, CommonErrors.UNEXPECTED.name(), CommonErrors.UNEXPECTED_ERROR_CODE),
                new HttpHeaders(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                request);
    }

    /**
     * Handles {@link QuizException} and its extensions.
     *
     * @param exception thrown API exception.
     * @param request   the current request details.
     * @return {@link Response} wrapped by {@link ResponseEntity}.
     */
    @ExceptionHandler(QuizException.class)
    public ResponseEntity<?> handleBasicException(QuizException exception, WebRequest request) {
        log.error(exception.getMessage());
        return handleExceptionInternal(
                exception,
                responseService.buildError(exception.getData(), exception.getGroup(), exception.getCode(), exception.getArgs()),
                new HttpHeaders(),
                exception.getStatusCode(),
                request);
    }

    /**
     * Handles {@link MethodArgumentNotValidException} thrown during DTOs validation.
     *
     * @param exception thrown validation exception.
     * @param headers   the current request headers.
     * @param status    default HTTP status.
     * @param request   the current request details.
     * @return {@link Response} wrapped by {@link ResponseEntity}.
     */
    @Override
    @SuppressWarnings("ALL")
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
                                                               HttpHeaders headers, HttpStatusCode status,
                                                               WebRequest request) {
        FieldError fieldError = exception.getBindingResult().getFieldError();
        String defaultMessage = fieldError.getDefaultMessage();
        String field = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, fieldError.getField());
        log.error("Field '{}' validation failed", field);
        return handleExceptionInternal(
                exception,
                responseService.buildError(null, CommonErrors.VALIDATION.name(), defaultMessage, field),
                new HttpHeaders(),
                HttpStatus.UNPROCESSABLE_ENTITY,
                request);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException exception, HttpHeaders headers,
                                                        HttpStatusCode status, WebRequest request) {
        Class<?> type = exception.getRequiredType();
        var message = INVALID_PARAMETER_TYPE_ERROR.formatted(exception.getPropertyName());
        var error = StringUtils.EMPTY;
        if (type != null && type.isEnum()) {
            var values = Arrays.stream(type.getEnumConstants()).toList();
            error = ResponseService.getMessage(
                    responseService.source(),
                    ENUM_VALUES_ERROR_CODE,
                    values.toString());
            message = message.concat(" - allowed values: " + values);
        }
        log.error(message, exception);
        return handleExceptionInternal(
                exception,
                responseService.buildError(null, CommonErrors.VALIDATION.name(),
                        ResponseService.API_ERROR_CODE_PREFIX + PARAMETER_ERROR_CODE,
                        Stream.of(exception.getPropertyName(), error).toArray(String[]::new)),
                new HttpHeaders(),
                HttpStatus.BAD_REQUEST,
                request
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException exception, WebRequest request) {
        var message = exception.getMessage() + ": '"
                + StringUtils.removeStart(request.getDescription(Boolean.FALSE), "uri=") + "'";
        log.error(message);
        return handleExceptionInternal(
                exception,
                responseService.buildError(null, CommonErrors.ACCESS.name(), CommonErrors.ACCESS_DENIED),
                new HttpHeaders(),
                HttpStatus.FORBIDDEN,
                request);
    }
}
