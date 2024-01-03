package com.quiz.javaquizapi.integration.facade.me;

import com.quiz.javaquizapi.dto.personal.AddressDto;
import com.quiz.javaquizapi.dto.personal.ContactDto;
import com.quiz.javaquizapi.dto.personal.PersonalInfoDto;
import com.quiz.javaquizapi.dto.personal.SocialMediaDto;
import com.quiz.javaquizapi.facade.mapping.Mapper;
import com.quiz.javaquizapi.facade.me.profile.personal.PersonalInfoFacade;
import com.quiz.javaquizapi.facade.me.profile.personal.QuizPersonalInfoFacade;
import com.quiz.javaquizapi.model.profile.personal.Address;
import com.quiz.javaquizapi.model.profile.personal.Contact;
import com.quiz.javaquizapi.model.profile.personal.PersonalInfo;
import com.quiz.javaquizapi.model.profile.personal.SocialMedia;
import com.quiz.javaquizapi.model.profile.personal.SocialType;
import com.quiz.javaquizapi.service.me.profile.PersonalInfoService;
import com.quiz.javaquizapi.service.me.profile.ProfileService;
import com.quiz.javaquizapi.service.me.profile.impl.personal.QuizAddressService;
import com.quiz.javaquizapi.service.me.profile.impl.personal.QuizContactService;
import com.quiz.javaquizapi.service.me.profile.impl.personal.QuizSocialMediaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

import static com.google.common.base.CaseFormat.LOWER_CAMEL;
import static com.google.common.base.CaseFormat.UPPER_CAMEL;
import static com.google.common.base.CaseFormat.UPPER_UNDERSCORE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Personal info facade tests")
public class PersonalInfoFacadeTests extends ProfileTests {
    private final PersonalInfo localInfo = new PersonalInfo().setProfile(getLocalProfile());
    private final Contact localContact = new Contact().setInfo(localInfo);

    {
        localContact.setCode(UUID.randomUUID().toString());
        localInfo.setCode(UUID.randomUUID().toString());
    }

    @Mock
    private PersonalInfoService infoService;
    @Mock
    private ProfileService profileService;
    @Mock
    private QuizAddressService addressService;
    @Mock
    private QuizContactService contactService;
    @Mock
    private QuizSocialMediaService mediaService;
    @Autowired
    private Mapper mapper;
    private PersonalInfoFacade facade;

    @Override
    @BeforeEach
    protected void setUp() {
        super.setUp();
        facade = new QuizPersonalInfoFacade(
                infoService,
                mapper,
                profileService,
                addressService,
                contactService,
                mediaService);
    }

    @Test
    @DisplayName("Fetch me by username")
    public void testFetchingMeGivenCurrentUsername() {
        when(infoService.getMe(localUser.getUsername())).thenReturn(localInfo);
        var me = facade.getMe(localUser.getUsername());
        assertThat(me).isNotNull();
        assertThat(me.getUsername()).isNull();
        assertThat(me.getProfileCode()).isEqualTo(getLocalProfile().getCode());
        assertThat(me.getBio()).isEqualTo(localInfo.getBio());
        verify(infoService).getMe(localUser.getUsername());
    }

    @Test
    @DisplayName("Fetch full by profile code")
    public void testFetchingFullGivenValidProfileCode() {
        when(infoService.getPersonalInfoByProfileCode(getLocalProfile().getCode()))
                .thenReturn(localInfo.setBio("testBio"));
        when(contactService.getByPersonalInfoCode(localInfo.getCode()))
                .thenReturn(localContact.setEmail("test@mail.com").setPhone("+7734223452398"));
        when(mediaService.getByContactCode(localContact.getCode()))
                .thenReturn(List.of(
                        buildMedia(SocialType.INSTAGRAM, localContact),
                        buildMedia(SocialType.VK, localContact)));
        when(addressService.getByPersonalInfoCode(localInfo.getCode()))
                .thenReturn(List.of(
                        new Address()
                                .setCountry("Kazakhstan")
                                .setCity("Almaty")
                                .setStreet("Abay 31/35")
                                .setRegion("Almaty")
                                .setPostalCode("A25D4T2")
                                .setInfo(localInfo),
                        new Address()
                                .setCountry("Kazakhstan")
                                .setCity("Almaty")
                                .setStreet("Valihanov 124/36")
                                .setRegion("Almaty")
                                .setPostalCode("A26F4A2")
                                .setInfo(localInfo)));
        var dto = facade.getFull(getLocalProfile().getCode());
        verify(infoService).getPersonalInfoByProfileCode(getLocalProfile().getCode());
        verify(contactService).getByPersonalInfoCode(localInfo.getCode());
        verify(addressService).getByPersonalInfoCode(localInfo.getCode());
        verify(mediaService).getByContactCode(localContact.getCode());
        assertThat(dto).isNotNull();
        assertThat(dto.getBio()).isEqualTo(localInfo.getBio());
        assertThat(dto.getContact()).isNotNull();
        assertThat(dto.getContact().getCode()).isEqualTo(localContact.getCode());
        assertThat(dto.getContact().getInfoCode()).isEqualTo(localInfo.getCode());
        assertThat(dto.getContact().getEmail()).isEqualTo(localContact.getEmail());
        assertThat(dto.getContact().getPhone()).isEqualTo(localContact.getPhone());
        assertThat(dto.getContact().getMedias()).hasSize(2);
        assertThat(dto.getAddresses()).hasSize(2);
        assertThat(captureLogs()).contains("Fetching a full personal info...");
    }

    @Test
    @DisplayName("Create a new personal info")
    public void testCreatingMeGivenCurrentUsername() {
        when(profileService.getMe(localUser.getUsername())).thenReturn(getLocalProfile());
        var dto = new PersonalInfoDto();
        dto.setUsername(localUser.getUsername());
        facade.create(dto);
        verify(profileService).getMe(localUser.getUsername());
        assertThat(dto.getUsername()).isNull();
        assertThat(captureLogs()).contains(
                "Creating an empty personal info for the current user...",
                "A personal info object successfully created.");
    }

    @Test
    @DisplayName("Update an existing personal info")
    public void testUpdatingPersonalInfoGivenValidData() {
        when(infoService.getMe(localUser.getUsername())).thenReturn(localInfo);
        var data = new PersonalInfoDto().setBio("test bio");
        data.setUsername(localUser.getUsername());
        facade.updateMe(data);
        var captor = ArgumentCaptor.forClass(PersonalInfo.class);
        verify(infoService).update(captor.capture());
        var actual = captor.getValue();
        assertThat(actual).isNotNull();
        assertThat(actual.getBio()).isEqualTo(data.getBio());
        assertThat(captureLogs()).contains(
                "Saving an updated personal info...",
                "Personal info successfully updated.");
    }

    @Test
    @DisplayName("Create user address")
    public void testCreatingUserAddressGivenValidData() {
        var data = new AddressDto().setCity("new").setInfoCode(localInfo.getCode());
        when(addressService.getEntityType()).thenReturn(Address.class);
        facade.updateAddress(data);
        var addressCaptor = ArgumentCaptor.forClass(Address.class);
        verify(addressService).update(addressCaptor.capture());
        var actual = addressCaptor.getValue();
        assertThat(actual).isNotNull();
        assertThat(actual.getCity()).isEqualTo(data.getCity());
        assertThat(captureLogs()).contains("Saving an instance of Address...", "Address saved successfully.");
    }

    @Test
    @DisplayName("Update user address")
    public void testUpdatingUserAddressGivenValidData() {
        var data = new AddressDto().setCity("new").setInfoCode(localInfo.getCode());
        data.setCode(UUID.randomUUID().toString());
        var entity = new Address().setCity("old").setInfo(localInfo);
        when(addressService.get(data.getCode())).thenReturn(entity);
        facade.updateAddress(data);
        var addressCaptor = ArgumentCaptor.forClass(Address.class);
        verify(addressService).update(addressCaptor.capture());
        var actual = addressCaptor.getValue();
        assertThat(actual).isNotNull();
        assertThat(actual.getCity()).isEqualTo(data.getCity());
        assertThat(captureLogs()).contains("Saving an instance of Address...", "Address saved successfully.");
    }

    @Test
    @DisplayName("Create user contact")
    public void testCreatingUserContactGivenValidData() {
        var data = new ContactDto().setEmail("new").setInfoCode(localInfo.getCode());
        when(contactService.getEntityType()).thenReturn(Contact.class);
        facade.updateContact(data);
        var contactCaptor = ArgumentCaptor.forClass(Contact.class);
        verify(contactService).update(contactCaptor.capture());
        var actual = contactCaptor.getValue();
        assertThat(actual).isNotNull();
        assertThat(actual.getEmail()).isEqualTo(data.getEmail());
        assertThat(captureLogs()).contains("Saving an instance of Contact...", "Contact saved successfully.");
    }

    @Test
    @DisplayName("Update user contact")
    public void testUpdatingUserContactGivenValidData() {
        var data = new ContactDto().setEmail("new").setInfoCode(localInfo.getCode());
        data.setCode(UUID.randomUUID().toString());
        var entity = new Contact().setEmail("old").setInfo(localInfo);
        when(contactService.get(data.getCode())).thenReturn(entity);
        facade.updateContact(data);
        var contactCaptor = ArgumentCaptor.forClass(Contact.class);
        verify(contactService).update(contactCaptor.capture());
        var actual = contactCaptor.getValue();
        assertThat(actual).isNotNull();
        assertThat(actual.getEmail()).isEqualTo(data.getEmail());
        assertThat(captureLogs()).contains("Saving an instance of Contact...", "Contact saved successfully.");
    }

    @Test
    @DisplayName("Create user contact social media")
    public void testCreatingUserContactSocialMediaGivenValidData() {
        var data = new SocialMediaDto().setAccountName("new").setContactCode(localContact.getCode());
        when(mediaService.getEntityType()).thenReturn(SocialMedia.class);
        facade.updateSocialMedia(data);
        var mediaCaptor = ArgumentCaptor.forClass(SocialMedia.class);
        verify(mediaService).update(mediaCaptor.capture());
        var actual = mediaCaptor.getValue();
        assertThat(actual).isNotNull();
        assertThat(actual.getAccountName()).isEqualTo(data.getAccountName());
        assertThat(captureLogs()).contains("Saving an instance of Social Media...", "Social Media saved successfully.");
    }

    @Test
    @DisplayName("Update user contact social media")
    public void testUpdatingUserContactSocialMediaGivenValidData() {
        var data = new SocialMediaDto().setAccountName("new").setContactCode(localContact.getCode());
        data.setCode(UUID.randomUUID().toString());
        var entity = new SocialMedia().setAccountName("old").setContact(localContact);
        when(mediaService.get(data.getCode())).thenReturn(entity);
        facade.updateSocialMedia(data);
        var mediaCaptor = ArgumentCaptor.forClass(SocialMedia.class);
        verify(mediaService).update(mediaCaptor.capture());
        var actual = mediaCaptor.getValue();
        assertThat(actual).isNotNull();
        assertThat(actual.getAccountName()).isEqualTo(data.getAccountName());
        assertThat(captureLogs()).contains("Saving an instance of Social Media...", "Social Media saved successfully.");
    }

    private SocialMedia buildMedia(SocialType type, Contact contact) {
        var name = UPPER_UNDERSCORE.to(UPPER_CAMEL, type.name());
        var accountName = "test".concat(name);
        var media = new SocialMedia()
                .setType(type)
                .setAccountName(accountName)
                .setLink(UPPER_UNDERSCORE.to(LOWER_CAMEL, name).concat(".com/").concat(accountName))
                .setContact(contact);
        media.setCode(UUID.randomUUID().toString());
        return media;
    }
}
