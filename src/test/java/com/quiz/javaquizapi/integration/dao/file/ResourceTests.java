package com.quiz.javaquizapi.integration.dao.file;

import com.quiz.javaquizapi.dao.ProfileRepository;
import com.quiz.javaquizapi.dao.ResourceRepository;
import com.quiz.javaquizapi.model.file.Resource;
import com.quiz.javaquizapi.model.file.ResourceFolder;
import com.quiz.javaquizapi.model.file.ResourceStatus;
import com.quiz.javaquizapi.model.profile.Profile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import java.util.Objects;
import java.util.UUID;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Resource DAO tests")
public class ResourceTests extends ParentResourceTests {
    public static final int PAGE_SIZE = 20;
    public static final int START_INCLUSIVE = 123456789;

    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private ProfileRepository profileRepository;

    private Profile localProfile;

    @BeforeEach
    void setUp() {
        super.setUp();
        localProfile = new Profile().setUser(localUser);
        localProfile.setCode(UUID.randomUUID().toString());
        profileRepository.save(localProfile);
        createResources(2, ResourceStatus.READY_FOR_REVIEW, getSecondLayer());
        createResources(2, ResourceStatus.READY_FOR_REVIEW, getRoot());
        createResources(5, ResourceStatus.APPROVED, getFirstLayer());
        createResources(3, ResourceStatus.SAVED);
    }

    @Test
    @DisplayName("Fetch approved page")
    public void testFetchingApprovedGivenPageParams() {
        var approved = resourceRepository.findAllApproved(Pageable.ofSize(PAGE_SIZE));
        assertThat(approved).isNotNull();
        assertThat(approved.get().count()).isEqualTo(5);
        assertTrue(approved.get().map(Resource::getStatus).allMatch(ResourceStatus.APPROVED::equals));
        assertTrue(approved.get().map(Resource::getApprovedBy).allMatch(localProfile::equals));
        assertTrue(approved.get().map(Resource::getFolder).allMatch(getFirstLayer()::equals));
    }

    @Test
    @DisplayName("Fetch ready for review")
    public void testFetchingReadyForReviewGivenPageParams() {
        var resources = resourceRepository.findAllReadyForReview(Pageable.ofSize(PAGE_SIZE));
        assertThat(resources).isNotNull();
        assertThat(resources.get().count()).isEqualTo(4);
        assertTrue(resources.get().map(Resource::getStatus).allMatch(ResourceStatus.READY_FOR_REVIEW::equals));
    }

    @Test
    @DisplayName("Fetch saved page")
    public void testFetchingSavedGivenPageParams() {
        var saved = resourceRepository.findByStatus(ResourceStatus.SAVED, Pageable.ofSize(PAGE_SIZE));
        assertThat(saved).isNotNull();
        assertThat(saved.get().count()).isEqualTo(3);
        assertTrue(saved.get().map(Resource::getStatus).allMatch(ResourceStatus.SAVED::equals));
        assertTrue(saved.get().map(Resource::getApprovedBy).allMatch(Objects::isNull));
        assertTrue(saved.get().map(Resource::getFolder).allMatch(getRoot()::equals));
    }

    @Test
    @DisplayName("Fetch by approver")
    public void testFetchingApprovedGivenApproverCode() {
        var approved = resourceRepository.findByApprovedByCode(localProfile.getCode(), Pageable.ofSize(PAGE_SIZE));
        assertThat(approved).isNotNull();
        assertThat(approved.get().count()).isEqualTo(5);
        assertTrue(approved.get().map(Resource::getStatus).allMatch(ResourceStatus.APPROVED::equals));
        assertTrue(approved.get().map(Resource::getApprovedBy).allMatch(localProfile::equals));
        assertTrue(approved.get().map(Resource::getFolder).allMatch(getFirstLayer()::equals));
    }

    @Test
    @DisplayName("Fetch by creator code")
    public void testFetchingResourcesGivenCreatorCode() {
        var resources = resourceRepository.findByCreatedByCode(localProfile.getCode(), Pageable.ofSize(PAGE_SIZE));
        assertThat(resources).isNotNull();
        assertThat(resources.get().count()).isEqualTo(12);
        assertThat(resources.get()
                .map(Resource::getStatus)
                .filter(ResourceStatus.APPROVED::equals)
                .count())
                .isEqualTo(5);
        assertThat(resources.get()
                .map(Resource::getStatus)
                .filter(ResourceStatus.SAVED::equals)
                .count())
                .isEqualTo(3);
        assertThat(resources.get()
                .map(Resource::getStatus)
                .filter(ResourceStatus.READY_FOR_REVIEW::equals)
                .count())
                .isEqualTo(4);
        assertTrue(resources.get().map(Resource::getCreatedBy).allMatch(localProfile::equals));
    }

    @Test
    @DisplayName("Fetch by folder path")
    public void testFetchingResourceGivenFolderPath() {
        var resources = resourceRepository.findByFolderPath(getSecondLayer().getPath(), Pageable.ofSize(PAGE_SIZE));
        assertThat(resources).isNotNull();
        assertThat(resources.get().count()).isEqualTo(2);
        assertTrue(resources.get().map(Resource::getStatus).allMatch(ResourceStatus.READY_FOR_REVIEW::equals));
        assertTrue(resources.get().map(Resource::getFolder).allMatch(getSecondLayer()::equals));
    }

    private void createResources(int number, ResourceStatus status) {
        createResources(number, status, getRoot());
    }

    private void createResources(int number, ResourceStatus status, ResourceFolder parent) {
        IntStream.range(START_INCLUSIVE, resolveNumber(number))
                .mapToObj(index -> initResource(index, status, parent)).forEach(resourceRepository::save);
    }

    private int resolveNumber(int number) {
        return START_INCLUSIVE + number;
    }

    private Resource initResource(int index, ResourceStatus status, ResourceFolder parent) {
        var resource = new Resource().setCreatedBy(localProfile).setResource_id(String.valueOf(index)).setStatus(status)
                .setFolder(parent);
        resource.setCode(UUID.randomUUID().toString());
        if (ResourceStatus.APPROVED.equals(status)) {
            resource.setApprovedBy(localProfile);
        }
        return resource;
    }
}
