package com.quiz.javaquizapi.service.user;

import com.quiz.javaquizapi.model.user.User;

/**
 * Provides functionality to operate with a {@link User}.
 */
public interface UserService {
    /**
     * Creates a new User.
     *
     * @param user user detail to save in the system.
     */
    void create(User user);
}
