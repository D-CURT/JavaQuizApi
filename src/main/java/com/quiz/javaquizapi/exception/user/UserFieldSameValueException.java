package com.quiz.javaquizapi.exception.user;

import org.springframework.http.HttpStatus;

/**
 * Thrown by the Quiz API when while user update check and the accepted value is already in use.
 */
public class UserFieldSameValueException extends UserException {
    /**
     * This constant provides an error code for the case when a user update value is already in use.
     * <p>The property key is - '<strong>api.errorCode.153</strong>'.
     * <p>See also: <strong>src/main/resources/messages.properties</strong>.
     */
    public static final int VALUE_IN_USE_CODE = 153;
    public static final String DEFAULT_ERROR = "Value already in use";

    public UserFieldSameValueException() {
        this(HttpStatus.CONFLICT, DEFAULT_ERROR, VALUE_IN_USE_CODE);
    }

    public UserFieldSameValueException(HttpStatus status, String message, int code, String... args) {
        super(status, message, code, args);
    }
}
