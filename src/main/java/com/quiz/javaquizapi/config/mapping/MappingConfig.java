package com.quiz.javaquizapi.config.mapping;

import com.quiz.javaquizapi.config.mapping.configurer.MappingConfigurer;
import com.quiz.javaquizapi.facade.mapping.Mapper;
import com.quiz.javaquizapi.facade.mapping.QuizModelMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class MappingConfig {

    private final ApplicationContext context;

    @Bean
    public Mapper mapper() {
        return configure(new QuizModelMapper());
    }

    public Mapper configure(QuizModelMapper mapper) {
        mapper.getConfiguration().setSkipNullEnabled(Boolean.TRUE);
        context.getBeansOfType(MappingConfigurer.class)
                .values()
                .forEach(mapping -> mapping.configure(mapper));
        return mapper;
    }
}
