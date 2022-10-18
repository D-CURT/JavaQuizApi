package com.quiz.javaquizapi.exception.user;

import com.quiz.javaquizapi.exception.QuizException;
import org.springframework.http.HttpStatus;

/**
 * The abstract user exception.
 * <p>Provides common logic for all of the {@link com.quiz.javaquizapi.model.user.User} related errors.
 */
public abstract class UserException extends QuizException {

    public UserException(HttpStatus status, String message, int code, String username) {
        super(status, message, code, username);
    }

    @Override
    protected String getGroup() {
        return "user";
    }
}
