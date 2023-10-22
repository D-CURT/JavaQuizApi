package com.quiz.javaquizapi.exception.profile.personal;

import org.springframework.http.HttpStatus;

/**
 * Thrown by the Quiz API when contacts of a user were already created.
 */
public class ContactAlreadyExistsException extends PersonalInfoException {

    /**
     * This constant provides an error code for the case when a user contacts already exist.
     * <p>The property key is - '<strong>api.errorCode.113</strong>'.
     * <p>See also: <strong>src/main/resources/messages.properties</strong>.
     */
    public static final int CONTACT_ALREADY_EXIST_ERROR_CODE = 113;

    public static final String DEFAULT_ERROR = "Contact already exists";

    public ContactAlreadyExistsException() {
        this(HttpStatus.CONFLICT, DEFAULT_ERROR, CONTACT_ALREADY_EXIST_ERROR_CODE);
    }

    public ContactAlreadyExistsException(HttpStatus status, String message, int code, String... args) {
        super(status, message, code, args);
    }
}
