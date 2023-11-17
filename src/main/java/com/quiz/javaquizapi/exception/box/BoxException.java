package com.quiz.javaquizapi.exception.box;

import com.quiz.javaquizapi.exception.QuizException;

/**
 * Thrown by the Quiz API when a Box error occurred.
 */
public abstract class BoxException extends QuizException {
    public BoxException(String message, int code, String... args) {
        super(message, code, args);
    }

    @Override
    protected String getGroup() {
        return "box";
    }
}
