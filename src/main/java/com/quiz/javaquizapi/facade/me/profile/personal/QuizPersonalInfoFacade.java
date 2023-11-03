package com.quiz.javaquizapi.facade.me.profile.personal;

import com.quiz.javaquizapi.annotation.Facade;
import com.quiz.javaquizapi.dto.PersonalInfoDto;
import com.quiz.javaquizapi.facade.mapping.Mapper;
import com.quiz.javaquizapi.facade.me.BaseMeFacade;
import com.quiz.javaquizapi.model.profile.personal.PersonalInfo;
import com.quiz.javaquizapi.service.me.MeService;
import com.quiz.javaquizapi.service.me.profile.PersonalInfoService;
import com.quiz.javaquizapi.service.me.profile.ProfileService;
import lombok.extern.slf4j.Slf4j;

import static com.quiz.javaquizapi.common.utils.GenericUtils.cast;

@Slf4j
@Facade
public class QuizPersonalInfoFacade extends BaseMeFacade<PersonalInfo, PersonalInfoDto> implements PersonalInfoFacade {
    private final ProfileService profileService;

    public QuizPersonalInfoFacade(MeService<PersonalInfo> service, Mapper mapper, ProfileService profileService) {
        super(service, mapper);
        this.profileService = profileService;
    }

    @Override
    public void create(PersonalInfoDto dto) {
        log.info("Creating an empty personal info for the current user...");
        PersonalInfo entity = mapper.map(dto, PersonalInfo.class);
        entity.setProfile(profileService.getMe(dto.getUsername()));
        service.create(entity);
        mapper.map(entity, dto);
        log.info("A personal info object successfully created.");
    }

    @Override
    public void updateMe(PersonalInfoDto data) {
        log.info("Saving an updated personal info...");
        var me = service.getMe(data.getUsername());
        mapper.map(data, me);
        cast(service, PersonalInfoService.class).update(me);
        log.info("Personal info successfully updated.");
    }
}
