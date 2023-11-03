package com.quiz.javaquizapi.service.mail;

import com.quiz.javaquizapi.model.user.UserUpdateCode;

/**
 * Provides functionality to change a user.
 */
public interface UserUpdateService {
    void sendCode(String toEmail);

    void updateUser(UserUpdateCode code);
}
