package com.quiz.javaquizapi.exception;

import com.quiz.javaquizapi.model.http.CommonErrors;
import org.springframework.http.HttpStatus;

/**
 * Thrown by the Quiz API when implicit validation failed.
 */
public class ValidationException extends QuizException {
    /**
     * This constant provides an error code for the case when a user has provided an invalid unique entity code.
     * <p>The property key is - '<strong>api.errorCode.42</strong>'
     * <p>See also: <strong>src/main/resources/messages.properties</strong>.
     */
    public static final int CODE_MALFORMED = 42;

    private ValidationException(String message, int code, String... args) {
        super(HttpStatus.UNPROCESSABLE_ENTITY, message, code, args);
    }

    /**
     * Creates an exception with the malformed code case.
     *
     * @param message technical error message.
     * @param args user-friendly description arguments.
     *
     * @return a {@link ValidationException} instance.
     */
    public static ValidationException getCodeMalformedException(String message, String... args) {
        return new ValidationException(message, CODE_MALFORMED, args);
    }

    @Override
    protected String getGroup() {
        return CommonErrors.VALIDATION.name();
    }
}
