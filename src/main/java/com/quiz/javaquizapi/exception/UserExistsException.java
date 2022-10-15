package com.quiz.javaquizapi.exception;

import org.springframework.http.HttpStatus;

/**
 * Thrown by the Quiz API to handle all the failures related to a {@link com.quiz.javaquizapi.model.user.User}.
 */
public class UserExistsException extends QuizException {

    /**
     * This constant provides an error code for the case when a user already exists.
     * <p>The property key is - '<strong>api.errorCode.20</strong>'.
     * <p>See also: <strong>src/main/resources/messages.properties</strong>.
     */
    public static final int USER_EXISTS_CODE = 20;

    public UserExistsException(String message, String username) {
        super(HttpStatus.CONFLICT, message, USER_EXISTS_CODE, username);
    }

    @Override
    protected String getGroup() {
        return "user_exists";
    }
}
