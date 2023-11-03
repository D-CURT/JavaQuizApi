package com.quiz.javaquizapi.integration.facade.me;

import com.quiz.javaquizapi.dto.ProfileDto;
import com.quiz.javaquizapi.facade.mapping.Mapper;
import com.quiz.javaquizapi.facade.me.profile.ProfileFacade;
import com.quiz.javaquizapi.facade.me.profile.QuizProfileFacade;
import com.quiz.javaquizapi.model.profile.Tiers;
import com.quiz.javaquizapi.service.me.profile.ProfileService;
import com.quiz.javaquizapi.service.me.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings("ALL")
@DisplayName("Profile facade tests")
public class ProfileFacadeTests extends ProfileTests {
    @Mock
    private ProfileService service;
    @Mock
    private UserService userService;
    @Autowired
    private Mapper mapper;
    private ProfileFacade facade;

    @Override
    @BeforeEach
    void setUp() {
        super.setUp();
        facade = new QuizProfileFacade(service, mapper, userService);
    }

    @Test
    @DisplayName("Fetch me by username")
    public void testFetchingMe() {
        when(service.getMe(localUser.getUsername())).thenReturn(getLocalProfile());
        ProfileDto me = facade.getMe(localUser.getUsername());
        assertThat(me).isNotNull();
        assertThat(me.getUsername()).isNull();
        assertThat(me.getTier()).isEqualTo(Tiers.MIDDLE);
        assertThat(me.getScore()).isEqualTo(getLocalProfile().getScore());
        assertThat(me.getRate()).isEqualTo(getLocalProfile().getRate());
        assertThat(me.getCode()).isEqualTo(getLocalProfile().getCode());
        assertThat(me.getUserCode()).isEqualTo(localUser.getCode());
        verify(service).getMe(localUser.getUsername());
    }

    @Test
    @DisplayName("Create profile")
    public void testCreationProfile() {
        when(userService.getMe(localUser.getUsername())).thenReturn(localUser);
        var dto = new ProfileDto();
        dto.setUsername(localUser.getUsername());
        facade.create(dto);
        verify(userService).getMe(localUser.getUsername());
        assertThat(dto.getUsername()).isNull();
        assertThat(captureLogs()).contains(
                "Crating a new trainee profile...",
                "Profile created successfully: tier - Trainee, score - 0, rate - 0.");
    }
}
