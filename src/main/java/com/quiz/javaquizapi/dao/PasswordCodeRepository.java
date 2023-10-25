package com.quiz.javaquizapi.dao;

import com.quiz.javaquizapi.model.user.PasswordCode;

import java.util.Optional;

/**
 * Accumulates all functionality of <strong>the user password code</strong> in the database.
 */
public interface PasswordCodeRepository extends BaseRepository<PasswordCode> {
    /**
     * Finds a password code by a username.
     *
     * @param username username field value.
     * @return {@link PasswordCode} if it exists. Optional field.
     */
    Optional<PasswordCode> findTopByUserUsername(String username);
}
