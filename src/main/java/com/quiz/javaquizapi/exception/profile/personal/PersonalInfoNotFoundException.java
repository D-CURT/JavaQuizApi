package com.quiz.javaquizapi.exception.profile.personal;

import com.quiz.javaquizapi.exception.QuizException;
import org.springframework.http.HttpStatus;

/**
 * Thrown by the Quiz API when a personal info object wasn't found.
 */
public class PersonalInfoNotFoundException extends QuizException {
    /**
     * This constant provides an error code for the case when a profile not found.
     * <p>The property key is - '<strong>api.errorCode.111</strong>'.
     * <p>See also: <strong>src/main/resources/messages.properties</strong>.
     */
    public static final int PERSONAL_INFO_ERROR_CODE = 111;

    /**
     * This constant provides an error code for the case when a profile not found.
     * <p>The property key is - '<strong>api.errorCode.112</strong>'.
     * <p>See also: <strong>src/main/resources/messages.properties</strong>.
     */
    public static final int PERSONAL_INFO_NO_ARGS_ERROR_CODE = 112;

    public static final String DEFAULT_ERROR = "Personal info not found";

    public PersonalInfoNotFoundException() {
        super(HttpStatus.NOT_FOUND, DEFAULT_ERROR, PERSONAL_INFO_NO_ARGS_ERROR_CODE);
    }

    public PersonalInfoNotFoundException(String username) {
        super(HttpStatus.NOT_FOUND, DEFAULT_ERROR, PERSONAL_INFO_ERROR_CODE, username);
    }
}
