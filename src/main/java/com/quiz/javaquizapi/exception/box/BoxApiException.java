package com.quiz.javaquizapi.exception.box;

import com.quiz.javaquizapi.exception.QuizException;

/**
 * Thrown by the Quiz API when an error occurred while requesting the Box API.
 */
public class BoxApiException extends QuizException {
    /**
     * This constant provides an error code for the case when the Box API error occurred.
     * <p>The property key is - '<strong>api.errorCode.200</strong>'.
     * <p>See also: <strong>src/main/resources/messages.properties</strong>.
     */
    public static final int BOX_API_ERROR_CODE = 200;

    public BoxApiException(String message) {
        super(message, BOX_API_ERROR_CODE, message);
    }

    @Override
    protected String getGroup() {
        return "box";
    }
}
