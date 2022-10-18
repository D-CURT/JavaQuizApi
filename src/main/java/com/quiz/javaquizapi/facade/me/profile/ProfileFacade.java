package com.quiz.javaquizapi.facade.me.profile;

import com.quiz.javaquizapi.dto.ProfileDto;
import com.quiz.javaquizapi.facade.me.MeFacade;
import com.quiz.javaquizapi.model.profile.Profile;

/**
 * Provides intermediary operations related to a {@link Profile}.
 */
public interface ProfileFacade extends MeFacade<Profile, ProfileDto> {
}
