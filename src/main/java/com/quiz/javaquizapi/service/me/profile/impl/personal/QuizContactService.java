package com.quiz.javaquizapi.service.me.profile.impl.personal;

import com.quiz.javaquizapi.dao.ContactRepository;
import com.quiz.javaquizapi.exception.profile.personal.ContactAlreadyExistsException;
import com.quiz.javaquizapi.exception.profile.personal.ContactNotFoundException;
import com.quiz.javaquizapi.model.profile.personal.Contact;
import com.quiz.javaquizapi.service.BaseQuizService;
import com.quiz.javaquizapi.service.me.profile.ContactService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuizContactService extends BaseQuizService<Contact> implements ContactService {

    private final ContactRepository repository;

    @Override
    public void create(Contact entity) {
        log.info("Checking if contacts for this user already was created...");
        if (repository.existsByInfoCode(entity.getInfo().getCode())) {
            throw new ContactAlreadyExistsException();
        }
        log.info("Saving a contact...");
        setCodeIfValid(entity);
        repository.save(entity);
    }

    @Override
    public Contact getByPersonalInfoCode(String code) {
        return repository.findByInfoCode(code)
                .orElseThrow(ContactNotFoundException::new);
    }

    @Override
    public boolean existsByPersonalInfoCode(String code) {
        return repository.existsByInfoCode(code);
    }
}
