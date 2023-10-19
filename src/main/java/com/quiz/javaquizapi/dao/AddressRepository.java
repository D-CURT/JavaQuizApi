package com.quiz.javaquizapi.dao;

import com.quiz.javaquizapi.model.profile.personal.Address;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends BaseRepository<Address>{

    Optional<List<Address>> findByInfoCode(String infoCode);
}
