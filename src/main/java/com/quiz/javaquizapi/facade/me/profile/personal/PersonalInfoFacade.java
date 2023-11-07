package com.quiz.javaquizapi.facade.me.profile.personal;

import com.quiz.javaquizapi.dto.personal.AddressDto;
import com.quiz.javaquizapi.dto.personal.ContactDto;
import com.quiz.javaquizapi.dto.personal.PersonalInfoDto;
import com.quiz.javaquizapi.dto.personal.PersonalInfoFullDto;
import com.quiz.javaquizapi.dto.personal.SocialMediaDto;
import com.quiz.javaquizapi.facade.me.MeFacade;
import com.quiz.javaquizapi.model.profile.personal.PersonalInfo;

/**
 * Provides intermediary operations related to a {@link PersonalInfo}.
 */
public interface PersonalInfoFacade extends MeFacade<PersonalInfo, PersonalInfoDto> {
    PersonalInfoFullDto getFull(String profileCode);

    void updateMe(PersonalInfoDto data);

    void updateAddress(AddressDto data);

    void updateContact(ContactDto data);

    void updateSocialMedia(SocialMediaDto data);
}
