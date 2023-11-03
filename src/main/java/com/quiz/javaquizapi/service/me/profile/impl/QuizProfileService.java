package com.quiz.javaquizapi.service.me.profile.impl;

import com.quiz.javaquizapi.dao.BaseRepository;
import com.quiz.javaquizapi.dao.ProfileRepository;
import com.quiz.javaquizapi.exception.profile.ProfileExistsException;
import com.quiz.javaquizapi.exception.profile.ProfileNotFoundException;
import com.quiz.javaquizapi.model.profile.Profile;
import com.quiz.javaquizapi.model.profile.Tiers;
import com.quiz.javaquizapi.service.me.BaseMeService;
import com.quiz.javaquizapi.service.me.profile.ProfileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.quiz.javaquizapi.common.utils.GenericUtils.cast;

/**
 * Provides functionality to operate with a user {@link Profile}.
 */
@Slf4j
@Service
public class QuizProfileService extends BaseMeService<Profile> implements ProfileService {
    public QuizProfileService(BaseRepository<Profile> repository) {
        super(repository);
    }

    @Override
    public Profile getMe(String username) {
        logFetchingEntity();
        return cast(getRepository(), ProfileRepository.class).findByUserUsername(username)
                .orElseThrow(() -> new ProfileNotFoundException(username));
    }

    @Override
    public Profile get(String code) {
        logFetchingByField(ENTITY_IDENTIFIER);
        return getRepository().findByCode(code).orElseThrow(ProfileNotFoundException::new);
    }

    @Override
    public void create(Profile entity) {
        log.info("Checking if profile for this user already was created...");
        if (cast(getRepository(), ProfileRepository.class).existsByUserCode(entity.getUser().getCode())) {
            throw new ProfileExistsException("A new profile cannot be created twice for one user",
                    entity.getUser().getUsername());
        }
        entity.setCode(UUID.randomUUID().toString());
        log.info("Applying TRAINEE tier for a new profile...");
        entity.setTier(Tiers.TRAINEE);
        entity.setScore(0L);
        entity.setRate(0L);
        getRepository().save(entity);
    }

    @Override
    public void update(Profile object) {
        log.info("Saving an updated profile...");
        getRepository().save(object);
    }
}
