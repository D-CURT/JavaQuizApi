package com.quiz.javaquizapi.exception.user;

import org.springframework.http.HttpStatus;

/**
 * Thrown by the Quiz API when a user update code expired.
 */
public class UserUpdateCodeExpiredException extends UserException {
    /**
     * This constant provides an error code for the case when a user update code expired.
     * <p>The property key is - '<strong>api.errorCode.152</strong>'.
     * <p>See also: <strong>src/main/resources/messages.properties</strong>.
     */
    public static final int CODE_EXPIRED = 152;
    public static final String DEFAULT_ERROR = "User update code is expired";

    public UserUpdateCodeExpiredException() {
        this(HttpStatus.BAD_REQUEST, DEFAULT_ERROR, CODE_EXPIRED);
    }

    public UserUpdateCodeExpiredException(HttpStatus status, String message, int code, String... args) {
        super(status, message, code, args);
    }
}
