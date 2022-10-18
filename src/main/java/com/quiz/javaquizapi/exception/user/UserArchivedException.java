package com.quiz.javaquizapi.exception.user;

import org.springframework.http.HttpStatus;

/**
 * Thrown by the Quiz API when the user archived.
 */
public class UserArchivedException extends UserException {
    /**
     * This constant provides an error code for the case when a user archived.
     * <p>The property key is - '<strong>api.errorCode.22</strong>'.
     * <p>See also: <strong>src/main/resources/messages.properties</strong>.
     */
    public static final int USER_ARCHIVED = 22;

    public UserArchivedException(String message, String username) {
        super(HttpStatus.FORBIDDEN, message, USER_ARCHIVED, username);
    }
}
