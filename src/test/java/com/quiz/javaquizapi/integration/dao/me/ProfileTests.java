package com.quiz.javaquizapi.integration.dao.me;

import com.quiz.javaquizapi.dao.ProfileRepository;
import com.quiz.javaquizapi.integration.dao.MeDaoTests;
import com.quiz.javaquizapi.model.profile.Profile;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@DisplayName("Profile DAO tests")
public class ProfileTests extends MeDaoTests<ProfileRepository> {

    private Profile testProfile;

    @BeforeEach
    void setUp() {
        testProfile = new Profile().setUser(localUser);
        testProfile.setCode(UUID.randomUUID().toString());
        getRepository().save(testProfile);
    }

    @Test
    @DisplayName("Fetch an existing profile by username of linked user")
    public void testFetchingExistingProfile() {
        getRepository().findByUserUsername(localUser.getUsername())
                .ifPresentOrElse(profile -> {
                    assertThat(profile).isNotNull();
                    assertThat(profile.getUser()).isEqualTo(localUser);
                    assertThat(profile.getCode()).isEqualTo(testProfile.getCode());
                }, () -> Assertions.fail("Profile not found"));
        assertExecutedQueries();
    }

    @Test
    @DisplayName("Fetch existing profile by incorrect username")
    public void testFetchingExistingProfileGivenIncorrectUsername() {
        assertThat(getRepository().findByUserUsername("Wrong username").isEmpty()).isTrue();
        assertExecutedQueries();
    }

    @Test
    @DisplayName("Check if profile exists by linked user code. Profile exists")
    public void testExistingUserByUsername() {
        assertThat(getRepository().existsByUserCode(localUser.getCode())).isTrue();
        assertExecutedQueries();
    }

    @Test
    @DisplayName("Check if profile exists by linked user code. Profile doesn't exist")
    public void testNonExistingUserByUsername() {
        assertThat(getRepository().existsByCode("Wrong code")).isFalse();
        assertExecutedQueries();
    }
}
