package com.quiz.javaquizapi.integration;

import com.quiz.javaquizapi.ApiTests;
import com.quiz.javaquizapi.config.mapping.MappingConfig;
import com.quiz.javaquizapi.config.mapping.configurer.AddressMappingConfigurer;
import com.quiz.javaquizapi.config.mapping.configurer.MeMappingConfigurer;
import com.quiz.javaquizapi.facade.mapping.QuizModelMapper;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@Import(MappingConfig.class)
@ExtendWith(MockitoExtension.class)
@SpringJUnitConfig(classes = {
        MeMappingConfigurer.class,
        AddressMappingConfigurer.class,
        QuizModelMapper.class})
public class ApiIntegrationTests extends ApiTests {
}
