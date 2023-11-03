package com.quiz.javaquizapi.service.me.profile;

import com.quiz.javaquizapi.model.profile.personal.PersonalInfo;
import com.quiz.javaquizapi.service.Updatable;
import com.quiz.javaquizapi.service.me.MeService;

/**
 * Provides functionality to operate with a personal info of a user {@link PersonalInfo}.
 */
public interface PersonalInfoService extends MeService<PersonalInfo>, Updatable<PersonalInfo> {
    PersonalInfo getPersonalInfoByProfileCode(String profileCode);

    boolean existsByProfileCode(String profileCode);
}
