package com.quiz.javaquizapi.integration.dao;

import com.quiz.javaquizapi.dao.BaseRepository;
import com.quiz.javaquizapi.dao.UserRepository;
import lombok.AccessLevel;
import lombok.Getter;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("all")
@Getter(AccessLevel.PROTECTED)
public abstract class MeDaoTests<R extends BaseRepository> extends DaoTests<R> {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    protected void initLocalUser() {
        userRepository.save(localUser);
    }
}
