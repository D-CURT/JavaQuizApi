package com.quiz.javaquizapi.dao;

import com.quiz.javaquizapi.model.user.UserUpdateCode;

import java.util.Optional;

/**
 * Accumulates all functionality of <strong>the user password code</strong> in the database.
 */
public interface UserUpdateCodeRepository extends BaseRepository<UserUpdateCode> {
    /**
     * Finds a password code by a username.
     *
     * @param username username field value.
     * @return {@link UserUpdateCode} if it exists. Optional field.
     */
    Optional<UserUpdateCode> findTopByUserUsernameOrderByCreatedAtDesc(String username);
}
