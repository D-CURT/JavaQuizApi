package com.quiz.javaquizapi.dao;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.quiz.javaquizapi.model.user.User;

/**
 * Accumulates all functionality of <strong>the user</strong> in the database.
 */
public interface UserRepository extends CrudRepository<User, Long> {
    /**
     * Finds a <strong>User</strong> by its username.
     *
     * @param username username field value.
     *
     * @return {@link User} if it exists. Optional field.
     */
    Optional<User> findByUsername(String username);

    /**
     * Checks if the <strong>User</strong> with an accepted username exists.
     *
     * @param username username field value.
     *
     * @return <strong>true</strong> if the user was found, <strong>false</strong> otherwise.
     */
    boolean existsByUsername(String username);
}
