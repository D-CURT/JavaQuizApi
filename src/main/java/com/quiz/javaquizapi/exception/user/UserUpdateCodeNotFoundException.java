package com.quiz.javaquizapi.exception.user;

import org.springframework.http.HttpStatus;

/**
 * Thrown by the Quiz API when an update code not found.
 */
public class UserUpdateCodeNotFoundException extends UserException {
    /**
     * This constant provides an error code for the case when a user update code not found by its code.
     * <p>The property key is - '<strong>api.errorCode.150</strong>'.
     * <p>See also: <strong>src/main/resources/messages.properties</strong>.
     */
    public static final int NOT_FOUND_NO_ARGS_CODE = 150;
    /**
     * This constant provides an error code for the case when a user update code not found by username.
     * <p>The property key is - '<strong>api.errorCode.151</strong>'.
     * <p>See also: <strong>src/main/resources/messages.properties</strong>.
     */
    public static final int NOT_FOUND_CODE = 151;

    public static final String DEFAULT_ERROR = "User update code not found";

    public UserUpdateCodeNotFoundException() {
        this(HttpStatus.NOT_FOUND, DEFAULT_ERROR, NOT_FOUND_NO_ARGS_CODE);
    }

    public UserUpdateCodeNotFoundException(String username) {
        this(HttpStatus.NOT_FOUND, DEFAULT_ERROR, NOT_FOUND_CODE, username);
    }

    public UserUpdateCodeNotFoundException(HttpStatus status, String message, int code, String... args) {
        super(status, message, code, args);
    }
}
