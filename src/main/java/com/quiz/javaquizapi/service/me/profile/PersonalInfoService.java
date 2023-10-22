package com.quiz.javaquizapi.service.me.profile;

import com.quiz.javaquizapi.model.profile.personal.PersonalInfo;
import com.quiz.javaquizapi.service.me.MeService;

/**
 * Provides functionality to operate with a personal info of a user {@link PersonalInfo}.
 */
public interface PersonalInfoService extends MeService<PersonalInfo> {

    PersonalInfo getPersonalInfoByProfileCode(String profileCode);

    PersonalInfo getPersonalInfo(String code);

    boolean existsByProfileCode(String profileCode);
}
