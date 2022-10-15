package com.quiz.javaquizapi.service.user.impl;

import com.quiz.javaquizapi.dao.UserRepository;
import com.quiz.javaquizapi.exception.UserExistsException;
import com.quiz.javaquizapi.exception.ValidationException;
import com.quiz.javaquizapi.model.user.Providers;
import com.quiz.javaquizapi.model.user.Roles;
import com.quiz.javaquizapi.model.user.User;
import com.quiz.javaquizapi.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

/**
 * Provides functionality to operate with a {@link User}.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class QuizUserService implements UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void create(User user) {
        log.info("Checking if a user with given username already exists...");
        if (repository.existsByUsername(user.getUsername())) {
            throw new UserExistsException("Unable to create a user, such username '%s' already exists"
                    .formatted(user.getUsername()), user.getUsername());
        }
        log.info("Resolving a new user unique code...");
        Optional.ofNullable(user.getCode()).filter(StringUtils::isNotBlank)
                .ifPresentOrElse(
                        value -> setCodeIfValid(user, value),
                        () -> user.setCode(UUID.randomUUID().toString()));
        log.info("Applying USER role...");
        Optional.ofNullable(user.getRole()).ifPresentOrElse(user::setRole, () -> user.setRole(Roles.USER));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        log.info("Applying LOCAL authentication provider...");
        user.setProvider(Providers.LOCAL);
        repository.save(user);
    }

    private void setCodeIfValid(User user, String code) {
        try {
            user.setCode(UUID.fromString(code).toString());
        } catch (Exception e) {
            throw ValidationException.getCodeMalformedException(
                    "Unable to create a new user: provided code is malformed, check its format - UUID is required.",
                    code);
        }
    }
}
