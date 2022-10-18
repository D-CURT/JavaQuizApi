package com.quiz.javaquizapi.facade.me.user;

import com.quiz.javaquizapi.dto.UserDto;
import com.quiz.javaquizapi.facade.me.MeFacade;
import com.quiz.javaquizapi.model.user.User;

/**
 * Provides intermediary operations related to a {@link User}.
 */
public interface UserFacade extends MeFacade<User, UserDto> {
}
