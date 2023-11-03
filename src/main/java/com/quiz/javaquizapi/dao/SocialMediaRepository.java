package com.quiz.javaquizapi.dao;

import com.quiz.javaquizapi.model.profile.personal.SocialMedia;
import com.quiz.javaquizapi.model.profile.personal.SocialType;

import java.util.List;
import java.util.Optional;

/**
 * Accumulates all functionality of <strong>the user social media</strong> in the database.
 */
public interface SocialMediaRepository extends BaseRepository<SocialMedia> {
    /**
     * Finds a <strong>SocialMedia</strong> by its contact code.
     *
     * @param contactCode contact code field value.
     * @return {@link SocialMedia} if it exists. Optional field.
     */
    Optional<List<SocialMedia>> findByContactCode(String contactCode);

    /**
     * Checks if the <strong>Social Medial</strong> with an accepted contact code and type exists.
     *
     * @param contactCode contact code field.
     * @param type type field value.
     * @return <strong>true</strong> if a social media was found, <strong>false</strong> otherwise.
     */
    boolean existsByContactCodeAndType(String contactCode, SocialType type);
}
