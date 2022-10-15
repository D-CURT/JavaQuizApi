package com.quiz.javaquizapi.model.http;

/**
 * Enumerates the most common error groups.
 */
public enum CommonErrors {
    UNEXPECTED, VALIDATION, AUTHENTICATION, ACCESS, AUTHORIZATION;

    /**
     * This constant provides an error code for the case when an unexpected error occurred.
     * <p>The property key is - '<strong>api.errorCode.0</strong>'.
     * <p>See also: <strong>src/main/resources/messages.properties</strong>.
     */
    public static final int UNEXPECTED_ERROR_CODE = 0;
    /**
     * This constant provides an error code for the case when incorrect data was provided by a user.
     * <p>The property key is - '<strong>api.errorCode.1</strong>'.
     * <p>See also: <strong>src/main/resources/messages.properties</strong>.
     */
    public static final int INCORRECT_INPUT_CODE = 1;
    /**
     * This constant provides an error code for the case when a user authentication is required.
     * <p>The property key is - '<strong>api.errorCode.2</strong>'
     * <p>See also: <strong>src/main/resources/messages.properties</strong>.
     */
    public static final int AUTHENTICATION_REQUIRED = 2;
    /**
     * This constant provides an error code for the case when a user has no access to the resource.
     * <p>The property key is - '<strong>api.errorCode.3</strong>'
     * <p>See also: <strong>src/main/resources/messages.properties</strong>.
     */
    public static final int ACCESS_DENIED = 3;
    /**
     * This constant provides an error code for the case when a user has provided incorrect credentials.
     * <p>The property key is - '<strong>api.errorCode.4</strong>'
     * <p>See also: <strong>src/main/resources/messages.properties</strong>.
     */
    public static final int BAD_CREDENTIALS = 4;
}
