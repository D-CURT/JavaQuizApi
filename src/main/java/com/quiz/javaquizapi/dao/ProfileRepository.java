package com.quiz.javaquizapi.dao;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.quiz.javaquizapi.model.profile.Profile;

/**
 * Accumulates all functionality of <strong>the user profile</strong> in the database.
 */
public interface ProfileRepository extends CrudRepository<Profile, Long> {
    /**
     * Finds a user <strong>Profile</strong> by username field of the user linked to this profile.
     *
     * @param username username field value
     *
     * @return {@link Profile} if it exists. Optional field.
     */
    Optional<Profile> findByUserUsername(String username);
}
