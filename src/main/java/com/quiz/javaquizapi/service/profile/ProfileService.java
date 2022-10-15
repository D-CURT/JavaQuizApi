package com.quiz.javaquizapi.service.profile;

import com.quiz.javaquizapi.model.profile.Profile;

/**
 * Provides functionality to operate with a user {@link Profile}.
 */
public interface ProfileService {
    /**
     * Creates an empty user Profile.
     *
     * @param username allows to find a User to link with this Profile.
     *
     * @return created profile.
     */
    Profile create(String username);
}
