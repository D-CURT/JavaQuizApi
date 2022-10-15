package com.quiz.javaquizapi.facade;

import com.quiz.javaquizapi.dto.UserDto;

/**
 * Provides intermediary operations related to a {@link com.quiz.javaquizapi.model.user.User}.
 */
public interface UserFacade {
    /**
     * Authorizes a new user in the system.
     *
     * @param dto user detail.
     */
    void authorize(UserDto dto);
}
