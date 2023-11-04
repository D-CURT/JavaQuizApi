package com.quiz.javaquizapi.service.me.profile.impl.personal;

import com.quiz.javaquizapi.dao.BaseRepository;
import com.quiz.javaquizapi.dao.ContactRepository;
import com.quiz.javaquizapi.exception.profile.personal.ContactAlreadyExistsException;
import com.quiz.javaquizapi.exception.profile.personal.ContactNotFoundException;
import com.quiz.javaquizapi.model.profile.personal.Contact;
import com.quiz.javaquizapi.service.BaseQuizService;
import com.quiz.javaquizapi.service.me.profile.ContactService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.quiz.javaquizapi.common.util.GenericUtils.cast;

@Slf4j
@Service
public class QuizContactService extends BaseQuizService<Contact> implements ContactService {
    public QuizContactService(BaseRepository<Contact> repository) {
        super(repository);
    }

    @Override
    public Contact get(String code) {
        logFetchingEntity();
        return getRepository().findByCode(code).orElseThrow(ContactNotFoundException::new);
    }

    @Override
    public void create(Contact entity) {
        log.info("Checking if contacts for this user already was created...");
        if (cast(getRepository(), ContactRepository.class).existsByInfoCode(entity.getInfo().getCode())) {
            throw new ContactAlreadyExistsException();
        }
        log.info("Saving a contact...");
        setCodeIfValid(entity);
        getRepository().save(entity);
    }

    @Override
    public Contact getByPersonalInfoCode(String code) {
        return cast(getRepository(), ContactRepository.class)
                .findByInfoCode(code)
                .orElseThrow(ContactNotFoundException::new);
    }

    @Override
    public boolean existsByPersonalInfoCode(String code) {
        return cast(getRepository(), ContactRepository.class).existsByInfoCode(code);
    }
}
