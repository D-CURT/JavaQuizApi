package com.quiz.javaquizapi.service.me.user.impl;

import com.quiz.javaquizapi.dao.PasswordCodeRepository;
import com.quiz.javaquizapi.exception.user.PasswordCodeNotFoundException;
import com.quiz.javaquizapi.model.user.PasswordCode;
import com.quiz.javaquizapi.service.me.BaseMeService;
import com.quiz.javaquizapi.service.me.user.PasswordCodeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Provides functionality to operate with a {@link PasswordCode}.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class QuizPasswordCodeService extends BaseMeService<PasswordCode> implements PasswordCodeService {
    public static final int PASSWORD_CODE_EXPIRATION_TIME_IN_MINUTES = 5;
    private final PasswordCodeRepository repository;

    @Override
    public PasswordCode get(String code) {
        logFetchingByField(ENTITY_IDENTIFIER);
        return repository.findByCode(code)
                .orElseThrow(PasswordCodeNotFoundException::new);
    }

    @Override
    public void create(PasswordCode entity) {
        setCodeIfValid(entity);
        log.info("Saving a password code...");
        repository.save(entity);
    }

    @Override
    public PasswordCode getMe(String username) {
        logFetchingEntity();
        return repository.findTopByUserUsernameOrderByCreatedAtDesc(username)
                .orElseThrow(() -> new PasswordCodeNotFoundException(username));
    }

    @Override
    public boolean isValid(PasswordCode code) {
        log.info("Validating password code...");
        return repository.findTopByUserUsernameOrderByCreatedAtDesc(code.getUser().getUsername())
                .filter(pass -> StringUtils.equals(code.getCheckNumber(), pass.getCheckNumber()))
                .map(pass -> pass
                        .getCreatedAt()
                        .isAfter(LocalDateTime.now().minusMinutes(PASSWORD_CODE_EXPIRATION_TIME_IN_MINUTES)))
                .orElseThrow(() -> new PasswordCodeNotFoundException(code.getUser().getUsername()));
    }
}
