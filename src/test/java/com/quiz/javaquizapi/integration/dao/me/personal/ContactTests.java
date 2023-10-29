package com.quiz.javaquizapi.integration.dao.me.personal;

import com.quiz.javaquizapi.dao.ContactRepository;
import com.quiz.javaquizapi.model.profile.personal.Contact;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Contact DAO tests")
public class ContactTests extends PersonalDaoTests<ContactRepository> {
    private Contact testContact;

    @Override
    @BeforeEach
    void setUp() {
        super.setUp();
        testContact = new Contact().setInfo(getTestInfo());
        testContact.setCode(UUID.randomUUID().toString());
        getRepository().save(testContact);
    }

    @Test
    @DisplayName("Fetch an contact by existing personal info code")
    public void testFetchingContactByPersonalInfoCode() {
        getRepository().findByInfoCode(INFO_CODE)
                .ifPresentOrElse(contact -> {
                    assertThat(contact).isNotNull();
                    assertThat(contact.getCode()).isEqualTo(testContact.getCode());
                    assertThat(contact.getInfo().getCode()).isEqualTo(INFO_CODE);
                }, () -> Assertions.fail("Contact not found"));
    }
}
