package com.quiz.javaquizapi.service.me.profile.impl.personal;

import com.quiz.javaquizapi.dao.AddressRepository;
import com.quiz.javaquizapi.dao.BaseRepository;
import com.quiz.javaquizapi.exception.profile.personal.AddressNotFoundException;
import com.quiz.javaquizapi.model.profile.personal.Address;
import com.quiz.javaquizapi.service.BaseUpdatableService;
import com.quiz.javaquizapi.service.me.profile.AddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import static com.quiz.javaquizapi.common.util.GenericUtils.cast;

/**
 * Provides functionality to operate with a user addresses objects {@link Address}.
 */
@Slf4j
@Service
public class QuizAddressService extends BaseUpdatableService<Address> implements AddressService {
    public QuizAddressService(BaseRepository<Address> repository) {
        super(repository);
    }

    @Override
    public Address get(String code) {
        logFetchingEntity();
        return getRepository().findByCode(code).orElseThrow(AddressNotFoundException::new);
    }

    @Override
    public List<Address> getByPersonalInfoCode(String code) {
        logFetchingByField("info code");
        return cast(getRepository(), AddressRepository.class).findByInfoCode(code).orElse(Collections.emptyList());
    }

    @Override
    public void update(Address object) {
        setCodeIfValid(object);
        getRepository().save(object);
    }
}
