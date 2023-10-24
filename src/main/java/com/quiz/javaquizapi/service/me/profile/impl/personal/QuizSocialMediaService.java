package com.quiz.javaquizapi.service.me.profile.impl.personal;

import com.quiz.javaquizapi.dao.SocialMediaRepository;
import com.quiz.javaquizapi.exception.profile.personal.SocialMediaAlreadyExistsException;
import com.quiz.javaquizapi.exception.profile.personal.SocialMediaNotFoundException;
import com.quiz.javaquizapi.model.profile.personal.SocialMedia;
import com.quiz.javaquizapi.model.profile.personal.SocialType;
import com.quiz.javaquizapi.service.BaseQuizService;
import com.quiz.javaquizapi.service.me.profile.SocialMediaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Provides functionality to operate with a user social media objects {@link SocialMedia}.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class QuizSocialMediaService extends BaseQuizService<SocialMedia> implements SocialMediaService {
    private final SocialMediaRepository repository;

    @Override
    public SocialMedia get(String code) {
        logFetchingEntity();
        return repository.findByCode(code).orElseThrow(SocialMediaNotFoundException::new);
    }

    @Override
    public void create(SocialMedia entity) {
        log.info("Checking if a social media object already exist...");
        if (repository.existsByContactCodeAndType(entity.getContact().getCode(), entity.getType())) {
            throw new SocialMediaAlreadyExistsException();
        }
        setCodeIfValid(entity);
        repository.save(entity);
    }

    @Override
    public List<SocialMedia> getByContactCode(String contactCode) {
        logFetchingByField("contact code");
        return repository.findByContactCode(contactCode)
                .orElseThrow(SocialMediaNotFoundException::new);
    }

    @Override
    public boolean existByContactCodeAndType(String contactCode, SocialType type) {
        logFetchingByField("contact code and type");
        return repository.existsByContactCodeAndType(contactCode, type);
    }
}
