package com.quiz.javaquizapi.service.me.profile;

import com.quiz.javaquizapi.model.profile.Profile;
import com.quiz.javaquizapi.service.Updatable;
import com.quiz.javaquizapi.service.me.MeService;

/**
 * Provides functionality to operate with a user {@link Profile}.
 */
public interface ProfileService extends MeService<Profile>, Updatable<Profile> {
}
