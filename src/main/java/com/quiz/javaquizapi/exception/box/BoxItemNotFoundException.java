package com.quiz.javaquizapi.exception.box;

import com.quiz.javaquizapi.exception.QuizException;
import org.springframework.http.HttpStatus;

/**
 * Thrown by the Quiz API when a Box item wasn't found.
 */
public class BoxItemNotFoundException extends QuizException {
    /**
     * This constant provides an error code for the case when a Box item wasn't found.
     * <p>The property key is - '<strong>api.errorCode.201</strong>'.
     * <p>See also: <strong>src/main/resources/messages.properties</strong>.
     */
    public static final int BOX_ITEM_ERROR_CODE = 201;
    public static final String DEFAULT_ERROR = "Box item not found";

    public BoxItemNotFoundException(String name) {
        super(HttpStatus.NOT_FOUND, DEFAULT_ERROR, BOX_ITEM_ERROR_CODE, name);
    }

    @Override
    protected String getGroup() {
        return "box";
    }
}
