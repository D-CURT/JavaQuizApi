package com.quiz.javaquizapi.facade.me.profile;

import com.quiz.javaquizapi.annotation.Facade;
import com.quiz.javaquizapi.dto.ProfileDto;
import com.quiz.javaquizapi.facade.mapping.Mapper;
import com.quiz.javaquizapi.facade.me.BaseMeFacade;
import com.quiz.javaquizapi.model.profile.Profile;
import com.quiz.javaquizapi.service.me.MeService;
import com.quiz.javaquizapi.service.me.user.UserService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Facade
public class QuizProfileFacade extends BaseMeFacade<Profile, ProfileDto> implements ProfileFacade {
    private final UserService userService;

    public QuizProfileFacade(MeService<Profile> service, Mapper mapper, UserService userService) {
        super(service, mapper);
        this.userService = userService;
    }

    @Override
    public void create(ProfileDto data) {
        log.info("Crating a new trainee profile...");
        Profile profile = mapper.map(data, Profile.class);
        profile.setUser(userService.getMe(data.getUsername()));
        service.create(profile);
        mapper.map(profile, data);
        log.info("Profile created successfully: tier - Trainee, score - 0, rate - 0.");
    }
}
