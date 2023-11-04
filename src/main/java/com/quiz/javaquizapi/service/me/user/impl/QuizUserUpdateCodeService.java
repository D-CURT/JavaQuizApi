package com.quiz.javaquizapi.service.me.user.impl;

import com.quiz.javaquizapi.dao.BaseRepository;
import com.quiz.javaquizapi.dao.UserUpdateCodeRepository;
import com.quiz.javaquizapi.exception.user.UserUpdateCodeNotFoundException;
import com.quiz.javaquizapi.model.user.UserUpdateCode;
import com.quiz.javaquizapi.service.me.BaseMeService;
import com.quiz.javaquizapi.service.me.user.UserUpdateCodeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.quiz.javaquizapi.common.util.GenericUtils.cast;

/**
 * Provides functionality to operate with a {@link UserUpdateCode}.
 */
@Slf4j
@Service
public class QuizUserUpdateCodeService extends BaseMeService<UserUpdateCode> implements UserUpdateCodeService {
    public static final int CODE_EXPIRATION_TIME_IN_MINUTES = 5;

    public QuizUserUpdateCodeService(BaseRepository<UserUpdateCode> repository) {
        super(repository);
    }

    @Override
    public UserUpdateCode get(String code) {
        logFetchingByField(ENTITY_IDENTIFIER);
        return getRepository().findByCode(code)
                .orElseThrow(UserUpdateCodeNotFoundException::new);
    }

    @Override
    public void create(UserUpdateCode entity) {
        setCodeIfValid(entity);
        log.info("Saving a user update code...");
        getRepository().save(entity);
    }

    @Override
    public UserUpdateCode getMe(String username) {
        logFetchingEntity();
        return cast(getRepository(), UserUpdateCodeRepository.class)
                .findTopByUserUsernameOrderByCreatedAtDesc(username)
                .orElseThrow(() -> new UserUpdateCodeNotFoundException(username));
    }

    @Override
    public boolean isValid(UserUpdateCode code) {
        log.info("Validating user update code...");
        return cast(getRepository(), UserUpdateCodeRepository.class)
                .findTopByUserUsernameOrderByCreatedAtDesc(code.getUser().getUsername())
                .filter(update -> StringUtils.equals(code.getCheckNumber(), update.getCheckNumber()))
                .map(update -> update
                        .getCreatedAt()
                        .isAfter(LocalDateTime.now().minusMinutes(CODE_EXPIRATION_TIME_IN_MINUTES)))
                .orElseThrow(() -> new UserUpdateCodeNotFoundException(code.getUser().getUsername()));
    }

    @Override
    public void update(UserUpdateCode object) {
        log.info("Saving updated user code");
        getRepository().save(object);
    }
}
