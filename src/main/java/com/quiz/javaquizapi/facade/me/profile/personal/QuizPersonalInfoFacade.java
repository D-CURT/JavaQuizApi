package com.quiz.javaquizapi.facade.me.profile.personal;

import com.quiz.javaquizapi.annotation.Facade;
import com.quiz.javaquizapi.common.util.GenericUtils;
import com.quiz.javaquizapi.dto.BaseDto;
import com.quiz.javaquizapi.dto.personal.AddressDto;
import com.quiz.javaquizapi.dto.personal.ContactDto;
import com.quiz.javaquizapi.dto.personal.ContactFullDto;
import com.quiz.javaquizapi.dto.personal.PersonalInfoDto;
import com.quiz.javaquizapi.dto.personal.PersonalInfoFullDto;
import com.quiz.javaquizapi.dto.personal.SocialMediaDto;
import com.quiz.javaquizapi.facade.mapping.Mapper;
import com.quiz.javaquizapi.facade.me.BaseMeFacade;
import com.quiz.javaquizapi.model.BaseEntity;
import com.quiz.javaquizapi.model.profile.personal.Address;
import com.quiz.javaquizapi.model.profile.personal.Contact;
import com.quiz.javaquizapi.model.profile.personal.PersonalInfo;
import com.quiz.javaquizapi.model.profile.personal.SocialMedia;
import com.quiz.javaquizapi.service.BaseUpdatableService;
import com.quiz.javaquizapi.service.me.MeService;
import com.quiz.javaquizapi.service.me.profile.AddressService;
import com.quiz.javaquizapi.service.me.profile.ContactService;
import com.quiz.javaquizapi.service.me.profile.PersonalInfoService;
import com.quiz.javaquizapi.service.me.profile.ProfileService;
import com.quiz.javaquizapi.service.me.profile.SocialMediaService;
import com.quiz.javaquizapi.service.me.profile.impl.personal.QuizAddressService;
import com.quiz.javaquizapi.service.me.profile.impl.personal.QuizContactService;
import com.quiz.javaquizapi.service.me.profile.impl.personal.QuizSocialMediaService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.function.BiFunction;

import static com.quiz.javaquizapi.common.util.GenericUtils.cast;
import static com.quiz.javaquizapi.common.util.TextUtils.beautifyDtoName;

@Slf4j
@Facade
public class QuizPersonalInfoFacade extends BaseMeFacade<PersonalInfo, PersonalInfoDto> implements PersonalInfoFacade {
    private final ProfileService profileService;
    private final AddressService addressService;
    private final ContactService contactService;
    private final SocialMediaService mediaService;

    public QuizPersonalInfoFacade(
            MeService<PersonalInfo> service,
            Mapper mapper,
            ProfileService profileService,
            AddressService addressService,
            ContactService contactService,
            SocialMediaService mediaService) {
        super(service, mapper);
        this.profileService = profileService;
        this.addressService = addressService;
        this.contactService = contactService;
        this.mediaService = mediaService;
    }

    @Override
    public PersonalInfoFullDto getFull(String profileCode) {
        log.info("Fetching a full personal info...");
        var info = cast(service(), PersonalInfoService.class).getPersonalInfoByProfileCode(profileCode);
        var dto = mapper().map(info, PersonalInfoFullDto.class);
        var contact = contactService.getByPersonalInfoCode(info.getCode());
        dto.setContact(mapper().map(contact, ContactFullDto.class));
        dto.getContact()
                .setMedias(mapper().mapList(mediaService.getByContactCode(contact.getCode()), SocialMediaDto.class));
        dto.setAddresses(mapper().mapList(addressService.getByPersonalInfoCode(info.getCode()), AddressDto.class));
        return dto;
    }

    @Override
    public void create(PersonalInfoDto dto) {
        log.info("Creating an empty personal info for the current user...");
        PersonalInfo entity = mapper().map(dto, PersonalInfo.class);
        entity.setProfile(profileService.getMe(dto.getUsername()));
        service().create(entity);
        mapper().map(entity, dto);
        log.info("A personal info object successfully created.");
    }

    @Override
    public void updateMe(PersonalInfoDto data) {
        log.info("Saving an updated personal info...");
        var me = service().getMe(data.getUsername());
        mapper().map(data, me);
        cast(service(), PersonalInfoService.class).update(me);
        log.info("Personal info successfully updated.");
    }

    @Override
    public void updateAddress(AddressDto data) {
        updatePersonalInfo(
                cast(addressService, QuizAddressService.class),
                data,
                Address::setInfo,
                service().get(data.getInfoCode()));
    }

    @Override
    public void updateContact(ContactDto data) {
        updatePersonalInfo(
                cast(contactService, QuizContactService.class),
                data,
                Contact::setInfo,
                service().get(data.getInfoCode()));
    }

    @Override
    public void updateSocialMedia(SocialMediaDto data) {
        updatePersonalInfo(
                cast(mediaService, QuizSocialMediaService.class),
                data,
                SocialMedia::setContact,
                contactService.get(data.getContactCode())
        );
    }

    private <E extends BaseEntity, D extends BaseDto, V> void updatePersonalInfo(
            BaseUpdatableService<E> personalService, D data, BiFunction<E, V, E> setter, V value) {
        var entityName = beautifyDtoName(data.getClass());
        log.info("Saving an instance of {}...", entityName);
        var entity =
                StringUtils.isBlank(data.getCode())
                        ? setter.apply(GenericUtils.create(personalService.getEntityType()), value)
                        : personalService.get(data.getCode());
        mapper().map(data, entity);
        personalService.update(entity);
        log.info("{} saved successfully.", entityName);
    }
}
