package com.quiz.javaquizapi.service.mail;

import com.quiz.javaquizapi.model.user.PasswordCode;

/**
 * Provides functionality to change a user password.
 */
public interface ChangePasswordService {
    void sendCode(String toEmail);

    void changePassword(PasswordCode code);
}
