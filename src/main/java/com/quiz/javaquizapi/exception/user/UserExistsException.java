package com.quiz.javaquizapi.exception.user;

import org.springframework.http.HttpStatus;

/**
 * Thrown by the Quiz API when a user with provided username already exists.
 */
public class UserExistsException extends UserException {

    /**
     * This constant provides an error code for the case when a user already exists.
     * <p>The property key is - '<strong>api.errorCode.20</strong>'.
     * <p>See also: <strong>src/main/resources/messages.properties</strong>.
     */
    public static final int USER_EXISTS_CODE = 20;

    public UserExistsException(String message, String username) {
        super(HttpStatus.CONFLICT, message, USER_EXISTS_CODE, username);
    }
}
