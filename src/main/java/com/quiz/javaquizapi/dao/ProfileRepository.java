package com.quiz.javaquizapi.dao;

import com.quiz.javaquizapi.model.profile.Profile;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * Accumulates all functionality of <strong>the user profile</strong> in the database.
 */
public interface ProfileRepository extends CrudRepository<Profile, Long> {
    /**
     * Finds a user <strong>Profile</strong> by username field of the user linked to this profile.
     *
     * @param username username field value
     * @return {@link Profile} if it exists. Optional field.
     */
    Optional<Profile> findByUserUsername(String username);

    /**
     * Checks if the profile already exists by its user code.
     *
     * @param userCode user code.
     * @return true if profile found.
     */
    boolean existsByUserCode(String userCode);
}
