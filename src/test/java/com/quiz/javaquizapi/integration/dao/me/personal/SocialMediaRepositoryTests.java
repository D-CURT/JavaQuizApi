package com.quiz.javaquizapi.integration.dao.me.personal;

import com.quiz.javaquizapi.dao.ContactRepository;
import com.quiz.javaquizapi.dao.SocialMediaRepository;
import com.quiz.javaquizapi.model.profile.personal.Contact;
import com.quiz.javaquizapi.model.profile.personal.SocialMedia;
import com.quiz.javaquizapi.model.profile.personal.SocialType;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Social media DAO tests")
public class SocialMediaRepositoryTests extends PersonalDaoTests<SocialMediaRepository> {

    @Autowired
    private ContactRepository contactRepository;
    private Contact testContact;

    @Override
    @BeforeEach
    void setUp() {
        super.setUp();

        testContact = new Contact().setInfo(getTestInfo());
        testContact.setCode(UUID.randomUUID().toString());
        contactRepository.save(testContact);

        SocialMedia media = new SocialMedia()
                .setContact(testContact)
                .setType(SocialType.VK);
        media.setCode(UUID.randomUUID().toString());
        getRepository().save(media);

        media = new SocialMedia()
                .setContact(testContact)
                .setType(SocialType.INSTAGRAM);
        media.setCode(UUID.randomUUID().toString());
        getRepository().save(media);
    }

    @Test
    @DisplayName("Fetch all of social media by existing contact code")
    public void testFetchingExistingMediaByContactCode() {
        getRepository().findByContactCode(testContact.getCode())
                .ifPresentOrElse(medias -> {
                    assertThat(medias).isNotNull();
                    assertTrue(CollectionUtils.isNotEmpty(medias));
                    assertThat(medias.size()).isEqualTo(2);
                }, () -> Assertions.fail("Social media not found"));
    }

    @Test
    @DisplayName("Check if a Social Media object exists in DB by its type")
    public void testExistingSocialMediaByItsType() {
        assertTrue(getRepository().existsByContactCodeAndType(testContact.getCode(), SocialType.INSTAGRAM));
    }
}
