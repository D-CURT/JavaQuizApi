package com.quiz.javaquizapi.facade.me.profile.personal;

import com.quiz.javaquizapi.dto.PersonalInfoDto;
import com.quiz.javaquizapi.facade.me.MeFacade;
import com.quiz.javaquizapi.model.profile.personal.PersonalInfo;

/**
 * Provides intermediary operations related to a {@link PersonalInfo}.
 */
public interface PersonalInfoFacade extends MeFacade<PersonalInfo, PersonalInfoDto> {
    void updateMe(PersonalInfoDto data);
}
