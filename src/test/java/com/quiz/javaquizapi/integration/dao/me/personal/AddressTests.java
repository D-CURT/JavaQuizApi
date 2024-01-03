package com.quiz.javaquizapi.integration.dao.me.personal;

import com.quiz.javaquizapi.dao.AddressRepository;
import com.quiz.javaquizapi.model.profile.personal.Address;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Address DAO tests")
public class AddressTests extends PersonalDaoTests<AddressRepository> {
    @Override
    @BeforeEach
    void setUp() {
        super.setUp();
        Address address = new Address().setInfo(getTestInfo());

        address.setCode(UUID.randomUUID().toString());
        getRepository().save(address);

        address = new Address().setInfo(getTestInfo());
        address.setCode(UUID.randomUUID().toString());
        getRepository().save(address);

        address = new Address().setInfo(getTestInfo());
        address.setCode(UUID.randomUUID().toString());
        getRepository().save(address);
    }

    @Test
    @DisplayName("Fetch all addresses entities belong to the test info")
    public void testFetchingAllAddressesByExistingPersonalInfoCode() {
        getRepository().findByInfoCode(INFO_CODE)
                .ifPresentOrElse(addresses -> {
                    assertThat(addresses).isNotNull();
                    assertTrue(CollectionUtils.isNotEmpty(addresses));
                    assertThat(addresses.size()).isEqualTo(3);
                }, () -> Assertions.fail("Addresses not found"));
        assertExecutedQueries();
    }
}
