package com.quiz.javaquizapi.exception.profile;

import org.springframework.http.HttpStatus;

/**
 * Thrown by the Quiz API when a profile wasn't found by its User username.
 */
public class ProfileNotFoundException extends ProfileException{
    /**
     * This constant provides an error code for the case when a profile not found.
     * <p>The property key is - '<strong>api.errorCode.101</strong>'.
     * <p>See also: <strong>src/main/resources/messages.properties</strong>.
     */
    public static final int PROFILE_NOT_FOUND_CODE = 101;

    /**
     * This constant provides an error code for the case when a profile not found.
     * <p>The property key is - '<strong>api.errorCode.102</strong>'.
     * <p>See also: <strong>src/main/resources/messages.properties</strong>.
     */
    public static final int PROFILE_NOT_FOUND_NO_ARGS_CODE = 102;
    public static final String DEFAULT_ERROR = "Profile not found";

    public ProfileNotFoundException() {
        super(HttpStatus.NOT_FOUND, DEFAULT_ERROR, PROFILE_NOT_FOUND_NO_ARGS_CODE);
    }

    public ProfileNotFoundException(String username) {
        super(HttpStatus.NOT_FOUND, DEFAULT_ERROR, PROFILE_NOT_FOUND_CODE, username);
    }
}
