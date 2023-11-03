package com.quiz.javaquizapi.service.me.profile.impl.personal;

import com.quiz.javaquizapi.dao.AddressRepository;
import com.quiz.javaquizapi.exception.profile.personal.AddressNotFoundException;
import com.quiz.javaquizapi.model.profile.personal.Address;
import com.quiz.javaquizapi.service.BaseQuizService;
import com.quiz.javaquizapi.service.me.profile.AddressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * Provides functionality to operate with a user addresses objects {@link Address}.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class QuizAddressService extends BaseQuizService<Address> implements AddressService {
    private final AddressRepository repository;

    @Override
    public Address get(String code) {
        logFetchingEntity();
        return repository.findByCode(code).orElseThrow(AddressNotFoundException::new);
    }

    @Override
    public void create(Address entity) {
        setCodeIfValid(entity);
        repository.save(entity);
    }

    @Override
    public List<Address> getByPersonalInfoCode(String code) {
        logFetchingByField("info code");
        return repository.findByInfoCode(code).orElse(Collections.emptyList());
    }
}
