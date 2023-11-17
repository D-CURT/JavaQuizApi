package com.quiz.javaquizapi.exception.box;

/**
 * Thrown by the Quiz API when a Box file is empty.
 */
public class BoxFileEmptyContentException extends BoxException {
    /**
     * This constant provides an error code for the case when the Box file is empty.
     * <p>The property key is - '<strong>api.errorCode.202</strong>'.
     * <p>See also: <strong>src/main/resources/messages.properties</strong>.
     */
    public static final int BOX_FILE_EMPTY_CODE = 202;
    public static final String DEFAULT_ERROR = "File content is empty";

    public BoxFileEmptyContentException() {
        super(DEFAULT_ERROR, BOX_FILE_EMPTY_CODE);
    }
}
