package com.quiz.javaquizapi.exception.user;

import org.springframework.http.HttpStatus;

/**
 * Thrown by the Quiz API when while password change check and the accepted password already in use.
 */
public class PasswordInUseException extends UserException {
    /**
     * This constant provides an error code for the case when a user password already in use.
     * <p>The property key is - '<strong>api.errorCode.153</strong>'.
     * <p>See also: <strong>src/main/resources/messages.properties</strong>.
     */
    public static final int PASSWORD_IN_USE_CODE = 153;
    public static final String DEFAULT_ERROR = "Password already in use";

    public PasswordInUseException() {
        this(HttpStatus.CONFLICT, DEFAULT_ERROR, PASSWORD_IN_USE_CODE);
    }

    public PasswordInUseException(HttpStatus status, String message, int code, String... args) {
        super(status, message, code, args);
    }
}
