package com.quiz.javaquizapi.config.mapping.configurer;

import com.quiz.javaquizapi.config.mapping.converter.PostAddressConverter;
import com.quiz.javaquizapi.dto.personal.AddressDto;
import com.quiz.javaquizapi.model.profile.personal.Address;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * Defines <strong>address</strong> conversion details.
 */
@Component
public class AddressMappingConfigurer implements MappingConfigurer {
    @Override
    public void configure(ModelMapper mapper) {
        TypeMapAggregator.of(Address.class, AddressDto.class)
                .apply(mapper)
                .postConvert(PostAddressConverter::new);
    }
}
