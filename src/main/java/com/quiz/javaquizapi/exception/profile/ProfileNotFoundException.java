package com.quiz.javaquizapi.exception.profile;

import org.springframework.http.HttpStatus;

/**
 * Thrown by the Quiz API when a profile wasn't found by its User username.
 */
public class ProfileNotFoundException extends ProfileException{
    /**
     * This constant provides an error code for the case when a profile not found.
     * <p>The property key is - '<strong>api.errorCode.61</strong>'.
     * <p>See also: <strong>src/main/resources/messages.properties</strong>.
     */
    public static final int PROFILE_NOT_FOUND_CODE = 61;
    public static final String DEFAULT_ERROR = "Profile not found";

    public ProfileNotFoundException(String username) {
        super(HttpStatus.NOT_FOUND, DEFAULT_ERROR, PROFILE_NOT_FOUND_CODE, username);
    }
}
