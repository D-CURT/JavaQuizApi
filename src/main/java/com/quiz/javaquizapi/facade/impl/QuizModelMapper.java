package com.quiz.javaquizapi.facade.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.quiz.javaquizapi.facade.Mapper;

/**
 * Converts JPA entities into DTOs and back.
 */
@Component
public class QuizModelMapper extends ModelMapper implements Mapper {
}
