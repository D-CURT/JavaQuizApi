package com.quiz.javaquizapi.facade.me.profile.personal;

import com.quiz.javaquizapi.annotation.Facade;
import com.quiz.javaquizapi.common.util.GenericUtils;
import com.quiz.javaquizapi.dto.BaseDto;
import com.quiz.javaquizapi.dto.personal.AddressDto;
import com.quiz.javaquizapi.dto.personal.PersonalInfoDto;
import com.quiz.javaquizapi.facade.mapping.Mapper;
import com.quiz.javaquizapi.facade.me.BaseMeFacade;
import com.quiz.javaquizapi.model.BaseEntity;
import com.quiz.javaquizapi.model.profile.personal.Information;
import com.quiz.javaquizapi.model.profile.personal.PersonalInfo;
import com.quiz.javaquizapi.service.BaseUpdatableService;
import com.quiz.javaquizapi.service.me.MeService;
import com.quiz.javaquizapi.service.me.profile.AddressService;
import com.quiz.javaquizapi.service.me.profile.PersonalInfoService;
import com.quiz.javaquizapi.service.me.profile.ProfileService;
import com.quiz.javaquizapi.service.me.profile.impl.personal.QuizAddressService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.TypeToken;

import static com.quiz.javaquizapi.common.util.GenericUtils.cast;
import static com.quiz.javaquizapi.common.util.TextUtils.beautifyDtoName;

@Slf4j
@Facade
public class QuizPersonalInfoFacade extends BaseMeFacade<PersonalInfo, PersonalInfoDto> implements PersonalInfoFacade {
    private final ProfileService profileService;
    private final AddressService addressService;

    public QuizPersonalInfoFacade(
            MeService<PersonalInfo> service,
            Mapper mapper,
            ProfileService profileService,
            AddressService addressService) {
        super(service, mapper);
        this.profileService = profileService;
        this.addressService = addressService;
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

    @Override
    public void updateAddress(AddressDto data) {
        updatePersonalInfo(cast(addressService, QuizAddressService.class), data, service.get(data.getInfoCode()));
    }

    private <E extends BaseEntity, D extends BaseDto> void updatePersonalInfo(
            BaseUpdatableService<E> personalService, D data, PersonalInfo info) {
        var entityName = beautifyDtoName(data.getClass());
        log.info("Saving an instance of {}...", entityName);
        var entity =
                StringUtils.isBlank(data.getCode())
                        ? instantiateInformation(personalService.getEntityType()).setInfo(info)
                        : personalService.get(data.getCode());
        mapper.map(data, entity);
        personalService.update(entity);
        log.info("{} saved successfully.", entityName);
    }

    private <E extends BaseEntity> Information<E> instantiateInformation(Class<E> type) {
        return cast(GenericUtils.create(type), new TypeToken<Information<E>>() {}.getRawType());
    }
}
