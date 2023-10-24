package com.quiz.javaquizapi.exception.profile.personal;

import org.springframework.http.HttpStatus;

/**
 * Thrown by the Quiz API when an address wasn't found.
 */
public class AddressNotFoundException extends PersonalInfoException {
    /**
     * This constant provides an error code for the case when a user address not found.
     * <p>The property key is - '<strong>api.errorCode.115</strong>'.
     * <p>See also: <strong>src/main/resources/messages.properties</strong>.
     */
    public static final int ADDRESS_NOT_FOUND_ERROR_CODE = 115;

    public static final String DEFAULT_ERROR = "AddressNotFound";

    public AddressNotFoundException() {
        this(HttpStatus.NOT_FOUND, DEFAULT_ERROR, ADDRESS_NOT_FOUND_ERROR_CODE);
    }

    public AddressNotFoundException(HttpStatus status, String message, int code, String... args) {
        super(status, message, code, args);
    }
}
