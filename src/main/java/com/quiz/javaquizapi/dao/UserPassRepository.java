package com.quiz.javaquizapi.dao;

import com.quiz.javaquizapi.model.user.UserPasswordCode;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserPassRepository extends CrudRepository<UserPasswordCode, Long> {

    /**
     * Finds a user <strong>UserPasswordCode</strong> by username.
     *
     * @param username username field value
     * @return {@link UserPasswordCode} if it exists. Optional field.
     */
    Optional<UserPasswordCode> findByUsername(String username);
}
