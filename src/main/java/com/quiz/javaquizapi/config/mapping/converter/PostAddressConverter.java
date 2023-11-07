package com.quiz.javaquizapi.config.mapping.converter;

import com.quiz.javaquizapi.dto.personal.AddressDto;
import com.quiz.javaquizapi.model.profile.personal.Address;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

public class PostAddressConverter implements Converter<Address, AddressDto> {
    @Override
    public AddressDto convert(MappingContext<Address, AddressDto> context) {
        var source = context.getSource();
        var formatted = String.join(", ",
                source.getStreet(),
                source.getCity().concat(" ").concat(source.getPostalCode()),
                source.getRegion(),
                source.getCountry());
        return context.getDestination().setFormattedAddress(formatted);
    }
}
