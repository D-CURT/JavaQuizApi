package com.quiz.javaquizapi.exception.profile.personal;

import com.quiz.javaquizapi.exception.QuizException;
import org.springframework.http.HttpStatus;

/**
 * The abstract personal info exception.
 * <p>Provides common logic for all of the {@link com.quiz.javaquizapi.model.profile.personal.PersonalInfo} related errors.
 */
public abstract class PersonalInfoException extends QuizException {

    public PersonalInfoException(HttpStatus status, String message, int code, String... args) {
        super(status, message, code, args);
    }

    @Override
    protected String getGroup() {
        return "Personal info";
    }
}
