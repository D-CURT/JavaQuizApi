package com.quiz.javaquizapi.exception.profile;

import com.quiz.javaquizapi.exception.QuizException;
import org.springframework.http.HttpStatus;

/**
 * The abstract profile exception.
 * <p>Provides common logic for all of the {@link com.quiz.javaquizapi.model.profile.Profile} related errors.
 */
public abstract class ProfileException extends QuizException {
    public ProfileException(HttpStatus status, String message, int code, String... args) {
        super(status, message, code, args);
    }

    @Override
    protected String getGroup() {
        return "profile";
    }
}
