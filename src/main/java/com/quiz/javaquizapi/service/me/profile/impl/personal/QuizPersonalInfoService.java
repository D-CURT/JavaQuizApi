package com.quiz.javaquizapi.service.me.profile.impl.personal;

import com.quiz.javaquizapi.dao.PersonalInfoRepository;
import com.quiz.javaquizapi.exception.profile.personal.PersonalInfoExistsException;
import com.quiz.javaquizapi.exception.profile.personal.PersonalInfoNotFoundException;
import com.quiz.javaquizapi.model.profile.Profile;
import com.quiz.javaquizapi.model.profile.personal.PersonalInfo;
import com.quiz.javaquizapi.service.me.BaseMeService;
import com.quiz.javaquizapi.service.me.profile.PersonalInfoService;
import com.quiz.javaquizapi.service.me.profile.ProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Provides functionality to operate with a personal info of a user {@link PersonalInfo}.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class QuizPersonalInfoService extends BaseMeService<PersonalInfo> implements PersonalInfoService {
    private final ProfileService profileService;
    private final PersonalInfoRepository repository;

    @Override
    public PersonalInfo getMe(String username) {
        logFetchingEntity();
        Profile me = profileService.getMe(username);
        return repository.findByProfileCode(me.getCode())
                .orElseThrow(() -> new PersonalInfoNotFoundException(username));
    }

    @Override
    public PersonalInfo get(String code) {
        logFetchingByField(ENTITY_IDENTIFIER);
        return repository.findByCode(code).orElseThrow(PersonalInfoNotFoundException::new);
    }

    @Override
    public void create(PersonalInfo entity) {
        Profile me = profileService.getMe(entity.getProfile().getUser().getUsername());
        if (existsByProfileCode(me.getCode())) {
            throw new PersonalInfoExistsException(me.getUser().getUsername());
        }
        log.info("Saving a personal info...");
        setCodeIfValid(entity);
        repository.save(entity);
    }

    @Override
    public PersonalInfo getPersonalInfoByProfileCode(String profileCode) {
        logFetchingByField("profile code");
        return repository.findByProfileCode(profileCode)
                .orElseThrow(PersonalInfoNotFoundException::new);
    }

    @Override
    public boolean existsByProfileCode(String profileCode) {
        log.info("Checking if personal info of the user already was created...");
        return repository.existsByProfileCode(profileCode);
    }
}
