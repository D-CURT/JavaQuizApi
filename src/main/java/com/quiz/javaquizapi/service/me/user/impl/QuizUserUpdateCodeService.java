package com.quiz.javaquizapi.service.me.user.impl;

import com.quiz.javaquizapi.dao.UserUpdateCodeRepository;
import com.quiz.javaquizapi.exception.user.UserUpdateCodeNotFoundException;
import com.quiz.javaquizapi.model.user.UserUpdateCode;
import com.quiz.javaquizapi.service.me.BaseMeService;
import com.quiz.javaquizapi.service.me.user.UserUpdateCodeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Provides functionality to operate with a {@link UserUpdateCode}.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class QuizUserUpdateCodeService extends BaseMeService<UserUpdateCode> implements UserUpdateCodeService {
    public static final int CODE_EXPIRATION_TIME_IN_MINUTES = 5;
    private final UserUpdateCodeRepository repository;

    @Override
    public UserUpdateCode get(String code) {
        logFetchingByField(ENTITY_IDENTIFIER);
        return repository.findByCode(code)
                .orElseThrow(UserUpdateCodeNotFoundException::new);
    }

    @Override
    public void create(UserUpdateCode entity) {
        setCodeIfValid(entity);
        log.info("Saving a user update code...");
        repository.save(entity);
    }

    @Override
    public UserUpdateCode getMe(String username) {
        logFetchingEntity();
        return repository.findTopByUserUsernameOrderByCreatedAtDesc(username)
                .orElseThrow(() -> new UserUpdateCodeNotFoundException(username));
    }

    @Override
    public boolean isValid(UserUpdateCode code) {
        log.info("Validating user update code...");
        return repository.findTopByUserUsernameOrderByCreatedAtDesc(code.getUser().getUsername())
                .filter(update -> StringUtils.equals(code.getCheckNumber(), update.getCheckNumber()))
                .map(update -> update
                        .getCreatedAt()
                        .isAfter(LocalDateTime.now().minusMinutes(CODE_EXPIRATION_TIME_IN_MINUTES)))
                .orElseThrow(() -> new UserUpdateCodeNotFoundException(code.getUser().getUsername()));
    }

    @Override
    public void update(UserUpdateCode object) {
        log.info("Saving updated user code");
        repository.save(object);
    }
}
