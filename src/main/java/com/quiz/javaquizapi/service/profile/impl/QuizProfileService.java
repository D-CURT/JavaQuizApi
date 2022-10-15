package com.quiz.javaquizapi.service.profile.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.quiz.javaquizapi.dao.ProfileRepository;
import com.quiz.javaquizapi.dao.UserRepository;
import com.quiz.javaquizapi.model.profile.Profile;
import com.quiz.javaquizapi.model.profile.Tiers;
import com.quiz.javaquizapi.service.profile.ProfileService;

/**
 * Provides functionality to operate with a user {@link Profile}.
 */
@Service
@RequiredArgsConstructor
public class QuizProfileService implements ProfileService {

    private final ProfileRepository repository;
    private final UserRepository userRepository;

    @Override
    public Profile create(String username) {
        return repository.save(
                new Profile()
                        .setScore(0L)
                        .setTier(Tiers.TRAINEE)
                        .setUser(userRepository.findByUsername(username).orElseThrow(RuntimeException::new))
        );
    }
}
