package com.quiz.javaquizapi.service.user.impl;

import com.quiz.javaquizapi.dao.UserRepository;
import com.quiz.javaquizapi.model.user.User;
import com.quiz.javaquizapi.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuizUserService implements UserService {

    private final UserRepository repository;

    @Override
    public void create(User user) {
        repository.save(user);
    }
}
