package com.quiz.javaquizapi.exception.user;

import org.springframework.http.HttpStatus;

/**
 * Thrown by the Quiz API when a password code not found.
 */
public class PasswordCodeNotFoundException extends UserException {
    /**
     * This constant provides an error code for the case when a user password code not found by its code.
     * <p>The property key is - '<strong>api.errorCode.150</strong>'.
     * <p>See also: <strong>src/main/resources/messages.properties</strong>.
     */
    public static final int PASSWORD_CODE_NOT_FOUND_NO_ARGS_CODE = 150;
    /**
     * This constant provides an error code for the case when a user password code not found by username.
     * <p>The property key is - '<strong>api.errorCode.151</strong>'.
     * <p>See also: <strong>src/main/resources/messages.properties</strong>.
     */
    public static final int PASSWORD_CODE_NOT_FOUND_CODE = 151;

    public static final String DEFAULT_ERROR = "Password code not found";

    public PasswordCodeNotFoundException() {
        this(HttpStatus.NOT_FOUND, DEFAULT_ERROR, PASSWORD_CODE_NOT_FOUND_NO_ARGS_CODE);
    }

    public PasswordCodeNotFoundException(String username) {
        this(HttpStatus.NOT_FOUND, DEFAULT_ERROR, PASSWORD_CODE_NOT_FOUND_CODE, username);
    }

    public PasswordCodeNotFoundException(HttpStatus status, String message, int code, String... args) {
        super(status, message, code, args);
    }
}
