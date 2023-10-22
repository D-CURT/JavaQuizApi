package com.quiz.javaquizapi.exception.profile.personal;

import org.springframework.http.HttpStatus;

/**
 * Thrown by the Quiz API when a social media object was already created.
 */
public class SocialMediaAlreadyExistsException extends PersonalInfoException {

    /**
     * This constant provides an error code for the case when a user social media already exist.
     * <p>The property key is - '<strong>api.errorCode.114</strong>'.
     * <p>See also: <strong>src/main/resources/messages.properties</strong>.
     */
    public static final int SOCIAL_MEDIA_ALREADY_EXISTS_ERROR_CODE = 114;

    public static final String DEFAULT_ERROR = "Social media of this type already exists";

    public SocialMediaAlreadyExistsException() {
        this(HttpStatus.CONFLICT, DEFAULT_ERROR, SOCIAL_MEDIA_ALREADY_EXISTS_ERROR_CODE);
    }

    public SocialMediaAlreadyExistsException(HttpStatus status, String message, int code, String... args) {
        super(status, message, code, args);
    }
}
