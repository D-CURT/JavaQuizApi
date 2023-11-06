package com.quiz.javaquizapi.unit.service.me;

import com.quiz.javaquizapi.common.util.ValidationUtils;
import com.quiz.javaquizapi.dao.PersonalInfoRepository;
import com.quiz.javaquizapi.exception.profile.ProfileNotFoundException;
import com.quiz.javaquizapi.exception.profile.personal.PersonalInfoExistsException;
import com.quiz.javaquizapi.exception.profile.personal.PersonalInfoNotFoundException;
import com.quiz.javaquizapi.model.profile.personal.PersonalInfo;
import com.quiz.javaquizapi.service.BaseQuizService;
import com.quiz.javaquizapi.service.me.profile.PersonalInfoService;
import com.quiz.javaquizapi.service.me.profile.ProfileService;
import com.quiz.javaquizapi.service.me.profile.impl.personal.QuizPersonalInfoService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Personal info service tests")
public class PersonalInfoServiceTests extends ProfileTests {
    private final PersonalInfo localInfo = new PersonalInfo().setProfile(getLocalProfile());

    {
        localInfo.setCode(UUID.randomUUID().toString());
    }

    @Mock
    private PersonalInfoRepository repository;
    @Mock
    private ProfileService profileService;
    private PersonalInfoService service;

    @Override
    @BeforeEach
    void setUp() {
        super.setUp();
        service = new QuizPersonalInfoService(repository, profileService);
    }

    @Test
    @DisplayName("Fetch me by username")
    public void testFetchingMe() {
        when(profileService.getMe(localUser.getUsername())).thenReturn(getLocalProfile());
        when(repository.findByProfileCode(getLocalProfile().getCode())).thenReturn(Optional.of(localInfo));
        PersonalInfo me = service.getMe(localUser.getUsername());
        assertThat(me).isNotNull();
        assertThat(me.getCode()).isEqualTo(localInfo.getCode());
        assertThat(me.getProfile()).isEqualTo(getLocalProfile());
        assertThat(me.getBio()).isEqualTo(localInfo.getBio());
        verify(repository).findByProfileCode(getLocalProfile().getCode());
        verify(profileService).getMe(localUser.getUsername());
        assertThat(captureLogs()).contains("Fetching a PersonalInfo by username...");
    }

    @Test
    @DisplayName("Fetch me by username when personal info doesn't exist")
    public void testFetchingMeGivenIncorrectUsernameThenTrowPersonalInfoNotFoundException() {
        when(profileService.getMe("Wrong username")).thenReturn(getLocalProfile());
        when(repository.findByProfileCode(getLocalProfile().getCode())).thenReturn(Optional.empty());
        var exception = Assertions.assertThrows(
                PersonalInfoNotFoundException.class,
                () -> service.getMe("Wrong username"));
        assertThat(exception.getCode()).isEqualTo(PersonalInfoNotFoundException.PERSONAL_INFO_ERROR_CODE);
        assertThat(exception.getReason()).isEqualTo(PersonalInfoNotFoundException.DEFAULT_ERROR);
        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(exception.getArgs()).contains("Wrong username");
        verify(profileService).getMe("Wrong username");
        verify(repository).findByProfileCode(getLocalProfile().getCode());
        assertThat(captureLogs()).contains("Fetching a PersonalInfo by username...");
    }

    @Test
    @DisplayName("Fetch me by username when profile doesn't exist")
    public void testFetchingMeGivenUsernameThenThrowPersonalInfoNotFoundException() {
        when(profileService.getMe("Wrong username")).thenThrow(new ProfileNotFoundException("Wrong username"));
        var exception =
                Assertions.assertThrows(ProfileNotFoundException.class, () -> service.getMe("Wrong username"));
        assertThat(exception.getReason()).isEqualTo(ProfileNotFoundException.DEFAULT_ERROR);
        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(exception.getArgs()).contains("Wrong username");
        assertThat(exception.getArgs()).hasSize(1);
        assertThat(captureLogs()).contains("Fetching a PersonalInfo by username...");
        verify(repository, never()).findByProfileCode(any(String.class));
    }

    @Test
    @DisplayName("Fetch personal info by code")
    public void testFetchingInfoGivenValidCode() {
        when(repository.findByCode(localInfo.getCode())).thenReturn(Optional.of(localInfo));
        PersonalInfo info = service.get(localInfo.getCode());
        assertThat(info).isNotNull();
        assertThat(info.getBio()).isEqualTo(localInfo.getBio());
        assertThat(info.getCode()).isEqualTo(localInfo.getCode());
        assertThat(info.getProfile()).isEqualTo(localInfo.getProfile());
        verify(repository).findByCode(localInfo.getCode());
        assertThat(captureLogs()).contains("Fetching a PersonalInfo by code...");
    }

    @Test
    @DisplayName("Fetch personal info by code when it doens't exist")
    public void testFetchingInfoGivenInvalidCodeThenThrowPersonalInfoNotFoundException() {
        when(repository.findByCode("Wrong code")).thenReturn(Optional.empty());
        var exception = Assertions.assertThrows(PersonalInfoNotFoundException.class, () -> service.get("Wrong code"));
        assertThat(exception.getReason()).isEqualTo(PersonalInfoNotFoundException.DEFAULT_ERROR);
        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(exception.getArgs()).hasSize(0);
        verify(repository).findByCode("Wrong code");
        assertThat(captureLogs()).contains("Fetching a PersonalInfo by code...");
    }

    @Test
    @DisplayName("Create a new personal info for the current user")
    public void testCreatingPersonalInfoGivenCurrentUsername() {
        when(profileService.getMe(localUser.getUsername())).thenReturn(getLocalProfile());
        when(repository.save(any(PersonalInfo.class))).thenReturn(localInfo);
        var expected = new PersonalInfo().setProfile(getLocalProfile());
        service.create(expected);
        var infoCaptor = ArgumentCaptor.forClass(PersonalInfo.class);
        verify(repository).existsByProfileCode(getLocalProfile().getCode());
        verify(repository).save(infoCaptor.capture());
        var actual = infoCaptor.getValue();
        assertThat(actual).isNotNull();
        assertThat(actual.getProfile()).isEqualTo(expected.getProfile());
        assertThat(ValidationUtils
                .validator(BaseQuizService.CODE_ERROR_FORMAT.formatted(PersonalInfo.class.getSimpleName()))
                .validateCode(actual.getCode())
        ).isNotBlank();
        assertThat(captureLogs()).contains(
                "Checking if personal info of the user already was created...",
                "Saving a personal info...");
    }

    @Test
    @DisplayName("Create a new personal info object which already exists")
    public void testCreatingNewInfoGivenCurrentUsernameAndAlreadyCreatedPersonalInfo() {
        when(repository.existsByProfileCode(getLocalProfile().getCode())).thenReturn(Boolean.TRUE);
        when(profileService.getMe(localUser.getUsername())).thenReturn(getLocalProfile());
        var expected = new PersonalInfo().setProfile(getLocalProfile());
        var exception = Assertions.assertThrows(PersonalInfoExistsException.class, () -> service.create(expected));
        assertThat(exception.getCode()).isEqualTo(PersonalInfoExistsException.PERSONAL_INFO_ERROR_CODE);
        assertThat(exception.getReason()).isEqualTo(PersonalInfoExistsException.DEFAULT_ERROR);
        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(exception.getArgs()).contains(localUser.getUsername());
        assertThat(exception.getArgs()).hasSize(1);
        assertThat(captureLogs()).contains("Checking if personal info of the user already was created...");
        verify(repository, never()).save(expected);
    }

    @Test
    @DisplayName("Fetch info by profile code")
    public void testFetchingInfoGivenProfileCode() {
        when(repository.findByProfileCode(getLocalProfile().getCode())).thenReturn(Optional.of(localInfo));
        var info = service.getPersonalInfoByProfileCode(getLocalProfile().getCode());
        assertThat(info).isNotNull();
        assertThat(info.getCode()).isEqualTo(localInfo.getCode());
        assertThat(info.getProfile()).isEqualTo(getLocalProfile());
        assertThat(captureLogs()).contains("Fetching a PersonalInfo by profile code...");
        verify(repository).findByProfileCode(getLocalProfile().getCode());
    }

    @Test
    @DisplayName("Fetch info by code when it doesn't exist")
    public void testFetchingInfoGivenNonexistentProfileCodeThenThrowPersonalInfoNotFoundException() {
        when(repository.findByProfileCode("Wrong code")).thenReturn(Optional.empty());
        var exception = Assertions.assertThrows(
                PersonalInfoNotFoundException.class,
                () -> service.getPersonalInfoByProfileCode("Wrong code"));
        assertThat(exception.getReason()).isEqualTo(PersonalInfoNotFoundException.DEFAULT_ERROR);
        assertThat(exception.getCode()).isEqualTo(PersonalInfoNotFoundException.PERSONAL_INFO_NO_ARGS_ERROR_CODE);
        assertThat(exception.getArgs()).hasSize(0);
        assertThat(captureLogs()).contains("Fetching a PersonalInfo by profile code...");
    }

    @Test
    @DisplayName("Update info")
    public void testUpdatingPersonalInfoGivenValidCode() {
        var info = new PersonalInfo();
        service.update(info);
        var infoCaptor = ArgumentCaptor.forClass(PersonalInfo.class);
        verify(repository).save(infoCaptor.capture());
        var actual = infoCaptor.getValue();
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(info);
        assertThat(captureLogs()).contains("Saving an updated personal info...");
    }
}
