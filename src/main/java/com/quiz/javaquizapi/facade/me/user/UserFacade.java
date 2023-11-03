package com.quiz.javaquizapi.facade.me.user;

import com.quiz.javaquizapi.dto.user.UserDto;
import com.quiz.javaquizapi.dto.user.UserUpdateCodeDto;
import com.quiz.javaquizapi.facade.me.MeFacade;
import com.quiz.javaquizapi.model.user.User;

/**
 * Provides intermediary operations related to a {@link User}.
 */
public interface UserFacade extends MeFacade<User, UserDto> {
    void sendCodeToChangeUser(String username);

    void updateMe(UserUpdateCodeDto dto);

    void updateRole(UserDto data);

    void archive(String code);
}
