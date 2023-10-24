package com.quiz.javaquizapi.exception.user;

import com.quiz.javaquizapi.exception.QuizException;
import org.springframework.http.HttpStatus;

/**
 * The abstract user exception.
 * <p>Provides common logic for all of the {@link com.quiz.javaquizapi.model.user.User} related errors.
 */
public abstract class UserException extends QuizException {
    public UserException(HttpStatus status, String message, int code, String... args) {
        super(status, message, code, args);
    }

    @Override
    protected String getGroup() {
        return "user";
    }
}
