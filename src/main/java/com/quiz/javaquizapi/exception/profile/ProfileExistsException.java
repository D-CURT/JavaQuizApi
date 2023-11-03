package com.quiz.javaquizapi.exception.profile;

import org.springframework.http.HttpStatus;

/**
 * Thrown by the Quiz API when a profile was already created for this User.
 */
public class ProfileExistsException extends ProfileException {
    /**
     * This constant provides an error code for the case when a profile already exists.
     * <p>The property key is - '<strong>api.errorCode.100</strong>'.
     * <p>See also: <strong>src/main/resources/messages.properties</strong>.
     */
    public static final int PROFILE_EXISTS_CODE = 100;

    public ProfileExistsException(String message, String username) {
        super(HttpStatus.CONFLICT, message, PROFILE_EXISTS_CODE, username);
    }
}
