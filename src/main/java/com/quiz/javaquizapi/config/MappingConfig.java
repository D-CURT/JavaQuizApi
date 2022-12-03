package com.quiz.javaquizapi.config;

import com.quiz.javaquizapi.dto.UserDto;
import com.quiz.javaquizapi.facade.mapping.Mapper;
import com.quiz.javaquizapi.facade.mapping.QuizModelMapper;
import com.quiz.javaquizapi.model.user.User;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MappingConfig {

    @Bean
    public Mapper mapper() {
        QuizModelMapper mapper = new QuizModelMapper();
        configure(mapper);
        return mapper;
    }

    public void configure(ModelMapper mapper) {
        // TODO implement flexible mapping configuration mechanism
        TypeMap<User, UserDto> map = mapper.createTypeMap(User.class, UserDto.class);
        map.addMappings(mapping -> mapping.using(converter -> null).map(User::getPassword, UserDto::setPassword));
    }
}
