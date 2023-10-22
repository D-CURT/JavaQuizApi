package com.quiz.javaquizapi.exception.profile.personal;

import com.quiz.javaquizapi.exception.QuizException;
import org.springframework.http.HttpStatus;

/**
 * Thrown by the Quiz API when a personal info of a user already exists.
 */
public class PersonalInfoExistsException extends QuizException {

    /**
     * This constant provides an error code for the case when a profile not found.
     * <p>The property key is - '<strong>api.errorCode.110</strong>'.
     * <p>See also: <strong>src/main/resources/messages.properties</strong>.
     */
    public static final int PERSONAL_INFO_ERROR_CODE = 110;

    public static final String DEFAULT_ERROR = "Personal info already exists";

    public PersonalInfoExistsException(String username) {
        super(HttpStatus.CONFLICT, DEFAULT_ERROR, PERSONAL_INFO_ERROR_CODE, username);
    }
}
