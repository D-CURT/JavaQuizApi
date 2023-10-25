package com.quiz.javaquizapi.exception.user;

import org.springframework.http.HttpStatus;

/**
 * Thrown by the Quiz API when password code expired.
 */
public class PasswordCodeExpiredException extends UserException {
    /**
     * This constant provides an error code for the case when a user password code expired.
     * <p>The property key is - '<strong>api.errorCode.152</strong>'.
     * <p>See also: <strong>src/main/resources/messages.properties</strong>.
     */
    public static final int PASSWORD_CODE_EXPIRED = 152;
    public static final String DEFAULT_ERROR = "Password code is expired";

    public PasswordCodeExpiredException() {
        this(HttpStatus.NOT_FOUND, DEFAULT_ERROR, PASSWORD_CODE_EXPIRED);
    }

    public PasswordCodeExpiredException(HttpStatus status, String message, int code, String... args) {
        super(status, message, code, args);
    }
}
