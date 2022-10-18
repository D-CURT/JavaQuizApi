package com.quiz.javaquizapi.service.me.profile.impl;

import com.quiz.javaquizapi.dao.ProfileRepository;
import com.quiz.javaquizapi.exception.profile.ProfileExistsException;
import com.quiz.javaquizapi.model.profile.Profile;
import com.quiz.javaquizapi.model.profile.Tiers;
import com.quiz.javaquizapi.service.me.BaseMeService;
import com.quiz.javaquizapi.service.me.profile.ProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.file.ProviderNotFoundException;
import java.util.UUID;

/**
 * Provides functionality to operate with a user {@link Profile}.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class QuizProfileService extends BaseMeService<Profile> implements ProfileService {
    private final ProfileRepository repository;

    @Override
    public Profile getMe(String username) {
        return getMe(username, (name) -> repository.findByUserUsername(name)
                .orElseThrow(() -> new ProviderNotFoundException(name)));
    }

    @Override
    public void create(Profile entity) {
        if (repository.existsByUserCode(entity.getUser().getCode())) {
            throw new ProfileExistsException("A new profile cannot be created twice for one user",
                    entity.getUser().getUsername());
        }
        entity.setCode(UUID.randomUUID().toString());
        entity.setTier(Tiers.TRAINEE);
        entity.setScore(0L);
        repository.save(entity);
    }
}
