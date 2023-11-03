package com.quiz.javaquizapi.service.me.profile.impl.personal;

import com.quiz.javaquizapi.dao.BaseRepository;
import com.quiz.javaquizapi.dao.SocialMediaRepository;
import com.quiz.javaquizapi.exception.profile.personal.SocialMediaAlreadyExistsException;
import com.quiz.javaquizapi.exception.profile.personal.SocialMediaNotFoundException;
import com.quiz.javaquizapi.model.profile.personal.SocialMedia;
import com.quiz.javaquizapi.model.profile.personal.SocialType;
import com.quiz.javaquizapi.service.BaseQuizService;
import com.quiz.javaquizapi.service.me.profile.SocialMediaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.quiz.javaquizapi.common.utils.GenericUtils.cast;

/**
 * Provides functionality to operate with a user social media objects {@link SocialMedia}.
 */
@Slf4j
@Service
public class QuizSocialMediaService extends BaseQuizService<SocialMedia> implements SocialMediaService {
    public QuizSocialMediaService(BaseRepository<SocialMedia> repository) {
        super(repository);
    }

    @Override
    public SocialMedia get(String code) {
        logFetchingEntity();
        return getRepository().findByCode(code).orElseThrow(SocialMediaNotFoundException::new);
    }

    @Override
    public void create(SocialMedia entity) {
        log.info("Checking if a social media object already exist...");
        if (cast(getRepository(), SocialMediaRepository.class).existsByContactCodeAndType(entity.getContact().getCode(), entity.getType())) {
            throw new SocialMediaAlreadyExistsException();
        }
        setCodeIfValid(entity);
        getRepository().save(entity);
    }

    @Override
    public List<SocialMedia> getByContactCode(String contactCode) {
        logFetchingByField("contact code");
        return cast(getRepository(), SocialMediaRepository.class)
                .findByContactCode(contactCode)
                .orElseThrow(SocialMediaNotFoundException::new);
    }

    @Override
    public boolean existByContactCodeAndType(String contactCode, SocialType type) {
        logFetchingByField("contact code and type");
        return cast(getRepository(), SocialMediaRepository.class).existsByContactCodeAndType(contactCode, type);
    }
}
