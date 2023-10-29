package com.quiz.javaquizapi.service.me.user;

import com.quiz.javaquizapi.model.user.UserUpdateCode;
import com.quiz.javaquizapi.service.Updatable;
import com.quiz.javaquizapi.service.me.MeService;

/**
 * Provides functionality to operate with a {@link UserUpdateCode}.
 */
public interface UserUpdateCodeService extends MeService<UserUpdateCode>, Updatable<UserUpdateCode> {
    boolean isValid(UserUpdateCode code);
}
