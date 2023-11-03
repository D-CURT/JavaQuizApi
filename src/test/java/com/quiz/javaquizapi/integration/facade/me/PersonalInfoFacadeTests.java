package com.quiz.javaquizapi.integration.facade.me;

import com.quiz.javaquizapi.dto.PersonalInfoDto;
import com.quiz.javaquizapi.facade.mapping.Mapper;
import com.quiz.javaquizapi.facade.me.profile.personal.PersonalInfoFacade;
import com.quiz.javaquizapi.facade.me.profile.personal.QuizPersonalInfoFacade;
import com.quiz.javaquizapi.model.profile.personal.PersonalInfo;
import com.quiz.javaquizapi.service.me.profile.PersonalInfoService;
import com.quiz.javaquizapi.service.me.profile.ProfileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Personal info facade tests")
public class PersonalInfoFacadeTests extends ProfileTests {
    private final PersonalInfo localInfo = new PersonalInfo().setProfile(getLocalProfile());

    {
        localInfo.setCode(UUID.randomUUID().toString());
    }

    @Mock
    private PersonalInfoService infoService;
    @Mock
    private ProfileService profileService;
    @Autowired
    private Mapper mapper;
    private PersonalInfoFacade facade;

    @Override
    @BeforeEach
    void setUp() {
        super.setUp();
        facade = new QuizPersonalInfoFacade(infoService, mapper, profileService);
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
}
