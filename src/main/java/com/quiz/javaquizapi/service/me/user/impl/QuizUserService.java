package com.quiz.javaquizapi.service.me.user.impl;

import com.quiz.javaquizapi.dao.BaseRepository;
import com.quiz.javaquizapi.dao.UserRepository;
import com.quiz.javaquizapi.exception.user.UserExistsException;
import com.quiz.javaquizapi.exception.user.UserNotFoundException;
import com.quiz.javaquizapi.model.user.Providers;
import com.quiz.javaquizapi.model.user.Roles;
import com.quiz.javaquizapi.model.user.User;
import com.quiz.javaquizapi.service.me.BaseMeService;
import com.quiz.javaquizapi.service.me.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.quiz.javaquizapi.common.util.GenericUtils.cast;

/**
 * Provides functionality to operate with a {@link User}.
 */
@Slf4j
@Service
public class QuizUserService extends BaseMeService<User> implements UserService {
    private final PasswordEncoder passwordEncoder;

    public QuizUserService(BaseRepository<User> repository, PasswordEncoder passwordEncoder) {
        super(repository);
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User getMe(String username) {
        logFetchingEntity();
        return cast(getRepository(), UserRepository.class)
                .findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    @Override
    public User get(String code) {
        logFetchingByField(ENTITY_IDENTIFIER);
        return getRepository().findByCode(code).orElseThrow(UserNotFoundException::new);
    }

    @Override
    public void create(User user) {
        log.info("Checking if a user with given username already exists...");
        if (cast(getRepository(), UserRepository.class).existsByUsername(user.getUsername())) {
            throw new UserExistsException("Unable to create a user, such username already exists", user.getUsername());
        }
        setCodeIfValid(user);
        log.info("Applying USER role...");
        Optional.ofNullable(user.getRole()).ifPresentOrElse(user::setRole, () -> user.setRole(Roles.USER));
        resolveDisplayName(user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        log.info("Applying LOCAL authentication provider...");
        user.setProvider(Providers.LOCAL);
        getRepository().save(user);
    }

    @Override
    public void update(User user) {
        log.info("Saving an updated user...");
        getRepository().save(user);
    }

    private void resolveDisplayName(User user) {
        log.info("Resolving a new user display name...");
        Optional.ofNullable(user.getDisplayName())
                .filter(StringUtils::isNotBlank)
                .ifPresentOrElse(
                        user::setDisplayName,
                        () -> user.setDisplayName(RandomStringUtils.random(10, true, true)));
    }
}
