package com.quiz.javaquizapi.service.me.user.impl;

import com.quiz.javaquizapi.dao.UserRepository;
import com.quiz.javaquizapi.exception.user.UserExistsException;
import com.quiz.javaquizapi.exception.user.UserNotFoundException;
import com.quiz.javaquizapi.model.user.Providers;
import com.quiz.javaquizapi.model.user.Roles;
import com.quiz.javaquizapi.model.user.User;
import com.quiz.javaquizapi.service.me.BaseMeService;
import com.quiz.javaquizapi.service.me.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Provides functionality to operate with a {@link User}.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class QuizUserService extends BaseMeService<User> implements UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User getMe(String username) {
        return getMe(username,
                name -> repository.findByUsername(name).orElseThrow(() -> new UserNotFoundException(name)));
    }

    @Override
    public void create(User user) {
        log.info("Checking if a user with given username already exists...");
        if (repository.existsByUsername(user.getUsername())) {
            throw new UserExistsException("Unable to create a user, such username already exists", user.getUsername());
        }
        setCodeIfValid(user, "Unable to create a new user: provided code is malformed, check its format - UUID is required.");
        log.info("Applying USER role...");
        Optional.ofNullable(user.getRole()).ifPresentOrElse(user::setRole, () -> user.setRole(Roles.USER));
        resolveDisplayName(user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        log.info("Applying LOCAL authentication provider...");
        user.setProvider(Providers.LOCAL);
        repository.save(user);
    }

    // TODO implement update display name

    private void resolveDisplayName(User user) {
        log.info("Resolving a new user display name...");
        Optional.ofNullable(user.getDisplayName())
                .filter(StringUtils::isNotBlank)
                .ifPresentOrElse(
                        user::setDisplayName,
                        () -> user.setDisplayName(RandomStringUtils.random(10, true, true)));
    }
}
