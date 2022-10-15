package com.quiz.javaquizapi.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static com.quiz.javaquizapi.model.http.CommonErrors.INCORRECT_INPUT_CODE;

/**
 * Thrown by the Quiz API to handle runtime failures.
 */
@Getter
public class QuizException extends ResponseStatusException {

    public static final String DEFAULT_GROUP = "api";

    private final int code;
    private final Object data;
    private final String group;
    private final String[] args;

    {
        this.group = getGroup();
    }

    /**
     * Creates an exception with the error message.
     * <p>The error code is default - 'INCORRECT_INPUT'.
     * <p>The HTTP status is default - 'BAD_REQUEST'.
     * <p>The error group specified separately, the default one is - 'api'.
     *
     * @param message technical error message.
     */
    public QuizException(String message) {
        this(message, INCORRECT_INPUT_CODE);
    }

    /**
     * Creates an exception with the error message and the HTTP status.
     * <p>The error code is default - 'INCORRECT_INPUT'.
     * <p>The HTTP status is default - 'BAD_REQUEST'.
     * <p>The error group specified separately, the default one is - 'api'.
     *
     * @param status response HTTP status.
     * @param message technical error message.
     */
    public QuizException(HttpStatus status, String message) {
        this(status, null, message, null, INCORRECT_INPUT_CODE);
    }

    /**
     * Creates an exception with the error message, error code, and arguments for a user-friendly description.
     * <p>The HTTP status is default - 'BAD_REQUEST'.
     * <p>The error group specified separately, the default one is - 'api'.
     *
     * @param message technical error message.
     * @param code user-friendly description code.
     * @param args user-friendly description arguments.
     */
    public QuizException(String message, int code, String... args) {
        this(HttpStatus.BAD_REQUEST, null, message, null, code, args);
    }

    /**
     * Creates an exception with the error message and the object that contains invalid data.
     * <p>The error code is default - 'INCORRECT_INPUT'.
     * <p>The HTTP status is default - 'BAD_REQUEST'.
     * <p>The error group specified separately, the default one is - 'api'.
     *
     * @param data failed object.
     * @param message technical error message.
     */
    public QuizException(Object data, String message) {
        this(data, message, INCORRECT_INPUT_CODE);
    }

    /**
     * Creates an exception with the error message, the HTTP status, code, and arguments for a user-friendly description.
     * <p>The error group specified separately, the default one is - 'api'.
     *
     * @param status response HTTP status.
     * @param message technical error message.
     * @param code user-friendly description code.
     * @param args user-friendly description arguments.
     */
    public QuizException(HttpStatus status, String message, int code, String... args) {
        this(status, null, message, null, code, args);
    }

    /**
     * Creates an exception with the error message, the object that contains invalid data, code,
     * and arguments for a user-friendly description.
     * <p>The HTTP status is default - 'BAD_REQUEST'.
     * <p>The error group specified separately, the default one is - 'api'.
     *
     * @param data failed object.
     * @param message technical error message.
     * @param code user-friendly description code.
     * @param args user-friendly description arguments.
     */
    public QuizException(Object data, String message, int code, String... args) {
        this(HttpStatus.BAD_REQUEST, data, message, null, code, args);
    }

    /**
     * Creates an exception with the error message, the object that contains invalid data, the HTTP status,
     * the original cause exception, code, and arguments for a user-friendly description.
     * <p>The error group specified separately, the default one is - 'api'.
     *
     * @param status response HTTP status.
     * @param data failed object.
     * @param message technical error message.
     * @param cause the original {@link Throwable} cause.
     * @param code user-friendly description code.
     * @param args user-friendly description arguments.
     */
    public QuizException(HttpStatus status, Object data, String message, Throwable cause, int code, String... args) {
        super(status, message, cause);
        this.code = code;
        this.data = data;
        this.args = args;
    }

    /**
     * Gets an error group of this exception. Override this method to <strong>provide your custom group</strong>
     * for the extension of this exception type.
     * <p>The value <strong>must be specified in the shake case</strong> format. The default group is - 'api'.
     *
     * @return the error group as a {@link String}.
     */
    protected String getGroup() {
        return DEFAULT_GROUP;
    }
}
