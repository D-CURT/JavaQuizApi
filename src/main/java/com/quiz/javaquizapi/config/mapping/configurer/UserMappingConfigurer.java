package com.quiz.javaquizapi.config.mapping.configurer;

import com.quiz.javaquizapi.dto.UserDto;
import com.quiz.javaquizapi.model.user.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * Defines <strong>user</strong> conversion details.
 */
@Component
public class UserMappingConfigurer implements MappingConfigurer {
    @Override
    public void configure(ModelMapper mapper) {
        TypeMapAggregator.of(User.class, UserDto.class)
                .apply(mapper)
                .aggregate(map -> map
                        .addMappings(mapping -> mapping
                                .using(converter -> null)
                                .map(User::getPassword, UserDto::setPassword)
                        )
                );
    }
}
