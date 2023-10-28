package com.quiz.javaquizapi.facade.me.user;

import com.quiz.javaquizapi.dto.user.UserDto;
import com.quiz.javaquizapi.dto.user.PasswordCodeDto;
import com.quiz.javaquizapi.facade.me.MeFacade;
import com.quiz.javaquizapi.model.user.User;
import com.quiz.javaquizapi.service.Updatable;

/**
 * Provides intermediary operations related to a {@link User}.
 */
public interface UserFacade extends MeFacade<User, UserDto>, Updatable<UserDto> {
    void sendCodeToChangePassword(String username);

    void changePassword(PasswordCodeDto dto);
}
