package com.quiz.javaquizapi.service.me.user;

import com.quiz.javaquizapi.model.user.PasswordCode;
import com.quiz.javaquizapi.service.me.MeService;

/**
 * Provides functionality to operate with a {@link PasswordCode}.
 */
public interface PasswordCodeService extends MeService<PasswordCode> {
    boolean isValid(PasswordCode code);
}
