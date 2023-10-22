package com.quiz.javaquizapi.exception.profile.personal;

import org.springframework.http.HttpStatus;

/**
 * Thrown by the Quiz API when a social media object wasn't found.
 */
public class SocialMediaNotFoundException extends PersonalInfoException {

    /**
     * This constant provides an error code for the case when a user social media not found.
     * <p>The property key is - '<strong>api.errorCode.115</strong>'.
     * <p>See also: <strong>src/main/resources/messages.properties</strong>.
     */
    public static final int SOCIAL_MEDIA_NOT_FOUND_EXCEPTION_CODE = 115;

    public static final String DEFAULT_ERROR = "Social media not found";

    public SocialMediaNotFoundException() {
        this(HttpStatus.NOT_FOUND, DEFAULT_ERROR, SOCIAL_MEDIA_NOT_FOUND_EXCEPTION_CODE);
    }

    public SocialMediaNotFoundException(HttpStatus status, String message, int code, String... args) {
        super(status, message, code, args);
    }
}
