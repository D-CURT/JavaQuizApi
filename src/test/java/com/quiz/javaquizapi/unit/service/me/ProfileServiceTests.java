package com.quiz.javaquizapi.unit.service.me;

import com.quiz.javaquizapi.ApiTests;
import com.quiz.javaquizapi.dao.ProfileRepository;
import com.quiz.javaquizapi.exception.profile.ProfileExistsException;
import com.quiz.javaquizapi.exception.profile.ProfileNotFoundException;
import com.quiz.javaquizapi.model.profile.Profile;
import com.quiz.javaquizapi.model.profile.Tiers;
import com.quiz.javaquizapi.service.me.profile.ProfileService;
import com.quiz.javaquizapi.service.me.profile.impl.QuizProfileService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Profile service tests")
@ExtendWith(MockitoExtension.class)
public class ProfileServiceTests extends ApiTests {

    private final Profile localProfile = new Profile()
            .setScore(123L)
            .setRate(12_000L)
            .setTier(Tiers.MIDDLE)
            .setUser(localUser);

    {
        localProfile.setCode(UUID.randomUUID().toString());
    }

    @Mock
    private ProfileRepository repository;

    private ProfileService service;

    @BeforeEach
    void setUp() {
        initLog();
        service = new QuizProfileService(repository);
    }

    @Test
    @DisplayName("Fetch me by username")
    public void testFetchingMe() {
        when(repository.findByUserUsername(localUser.getUsername())).thenReturn(Optional.of(localProfile));
        Profile me = service.getMe(localUser.getUsername());
        assertThat(me).isNotNull();
        assertThat(me.getUser()).isEqualTo(localUser);
        assertThat(me.getUser().getUsername()).isEqualTo(localUser.getUsername());
        assertThat(me.getCode()).isEqualTo(localProfile.getCode());
        assertThat(me.getScore()).isEqualTo(localProfile.getScore());
        assertThat(me.getRate()).isEqualTo(localProfile.getRate());
        assertThat(me.getTier()).isEqualTo(localProfile.getTier());
        verify(repository).findByUserUsername(localUser.getUsername());
        assertThat(captureLogs()).contains("Fetching a Profile by username...");
    }

    @Test
    @DisplayName("Fetch me by username when profile doesn't exist")
    public void testFetchingMeGivenIncorrectUsername() {
        when(repository.findByUserUsername("Wrong username")).thenReturn(Optional.empty());
        ProfileNotFoundException exception =
                Assertions.assertThrows(ProfileNotFoundException.class, () -> service.getMe("Wrong username"));
        assertThat(exception.getCode()).isEqualTo(ProfileNotFoundException.PROFILE_NOT_FOUND_CODE);
        assertThat(exception.getReason()).isEqualTo(ProfileNotFoundException.DEFAULT_ERROR);
        assertThat(exception.getArgs()).contains("Wrong username");
        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        verify(repository).findByUserUsername("Wrong username");
        assertThat(captureLogs()).contains("Fetching a Profile by username...");
    }

    @Test
    @DisplayName("Create a new profile for the current user")
    public void testCreationNewProfileGivenCurrentUsername() {
        Profile expected = new Profile().setUser(localUser);
        when(repository.save(any(Profile.class))).thenReturn(expected);
        service.create(expected);
        ArgumentCaptor<Profile> profileCaptor = ArgumentCaptor.forClass(Profile.class);
        verify(repository).existsByUserCode(localUser.getCode());
        verify(repository).save(profileCaptor.capture());
        Profile actual = profileCaptor.getValue();
        assertThat(actual).isNotNull();
        assertThat(actual.getTier()).isEqualTo(Tiers.TRAINEE);
        assertThat(actual.getScore()).isEqualTo(0L);
        assertThat(actual.getRate()).isEqualTo(0L);
        assertThat(actual.getCode()).isNotBlank();
        assertThat(captureLogs()).contains(
                "Checking if profile for this user already was created...",
                "Applying TRAINEE tier for a new profile...");
    }

    @Test
    @DisplayName("Unable to create more then one profile per user")
    public void testCreatingProfileGivenUsernameForWhichProfileAlreadyCreated() {
        Profile expected = new Profile().setUser(localUser);
        when(repository.existsByUserCode(localUser.getCode())).thenReturn(Boolean.TRUE);
        ProfileExistsException exception =
                Assertions.assertThrows(ProfileExistsException.class, () -> service.create(expected));
        assertThat(exception.getCode()).isEqualTo(ProfileExistsException.PROFILE_EXISTS_CODE);
        assertThat(exception.getArgs()).contains(localUser.getUsername());
        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @AfterEach
    void tearDown() {
        clearLog();
    }
}
