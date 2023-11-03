package com.quiz.javaquizapi.exception.mail;

import com.quiz.javaquizapi.exception.QuizException;
import org.springframework.http.HttpStatus;

/**
 * Thrown by the Quiz API when an error occurred while sending an email.
 */
public class QuizMailException extends QuizException {
    /**
     * This constant provides an error code for the case when a mail error occurred.
     * <p>The property key is - '<strong>api.errorCode.120</strong>'.
     * <p>See also: <strong>src/main/resources/messages.properties</strong>.
     */
    public static final int MAIL_ERROR_CODE = 120;

    public QuizMailException(String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, message, MAIL_ERROR_CODE, message);
    }
}
