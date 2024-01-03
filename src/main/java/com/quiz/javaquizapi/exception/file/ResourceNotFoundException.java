package com.quiz.javaquizapi.exception.file;

import com.quiz.javaquizapi.exception.QuizException;
import org.springframework.http.HttpStatus;

/**
 * Thrown by the Quiz API when a resource not found.
 */
public class ResourceNotFoundException extends QuizException {
    /**
     * This constant provides an error code for the case when the resource not found.
     * <p>The property key is - '<strong>api.errorCode.210</strong>'.
     * <p>See also: <strong>src/main/resources/messages.properties</strong>.
     */
    public static final int RESOURCE_NOT_FOUND_CODE = 210;
    public static final String DEFAULT_ERROR = "Resource not found";

    public ResourceNotFoundException() {
        super(HttpStatus.NOT_FOUND, DEFAULT_ERROR, RESOURCE_NOT_FOUND_CODE);
    }

    public ResourceNotFoundException(HttpStatus status, String message, int code) {
        super(status, message, code);
    }

    @Override
    protected String getGroup() {
        return "resource";
    }
}
