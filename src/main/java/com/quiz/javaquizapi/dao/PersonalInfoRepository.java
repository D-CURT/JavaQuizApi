package com.quiz.javaquizapi.dao;

import com.quiz.javaquizapi.model.profile.personal.PersonalInfo;

import java.util.Optional;

/**
 * Accumulates all functionality of <strong>the user personal info</strong> in the database.
 */
public interface PersonalInfoRepository extends BaseRepository<PersonalInfo> {
    /**
     * Finds a <strong>Personal Info</strong> by its profile code.
     *
     * @param profileCode profile code field value.
     * @return {@link PersonalInfo} if it exists. Optional field.
     */
    Optional<PersonalInfo> findByProfileCode(String profileCode);

    /**
     * Checks if the <strong>Personal Info</strong> with an accepted profile code exists.
     *
     * @param profileCode profile code field value.
     * @return <strong>true</strong> if a personal info object was found, <strong>false</strong> otherwise.
     */
    boolean existsByProfileCode(String profileCode);
}
