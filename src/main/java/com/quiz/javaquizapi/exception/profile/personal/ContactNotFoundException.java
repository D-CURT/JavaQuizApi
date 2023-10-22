package com.quiz.javaquizapi.exception.profile.personal;

import org.springframework.http.HttpStatus;

/**
 * Thrown by the Quiz API when a user contacts weren't found.
 */
public class ContactNotFoundException extends PersonalInfoException {

    /**
     * This constant provides an error code for the case when a user contacts not found.
     * <p>The property key is - '<strong>api.errorCode.114</strong>'.
     * <p>See also: <strong>src/main/resources/messages.properties</strong>.
     */
    public static final int CONTACT_NOT_FOUND_ERROR_CODE = 114;

    public static final String DEFAULT_ERROR = "Contact not found";

    public ContactNotFoundException() {
        this(HttpStatus.NOT_FOUND, DEFAULT_ERROR, CONTACT_NOT_FOUND_ERROR_CODE);
    }

    public ContactNotFoundException(HttpStatus status, String message, int code, String... args) {
        super(status, message, code, args);
    }
}
