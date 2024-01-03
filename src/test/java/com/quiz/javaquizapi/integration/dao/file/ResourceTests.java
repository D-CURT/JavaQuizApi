package com.quiz.javaquizapi.integration.dao.file;

import com.quiz.javaquizapi.dao.ProfileRepository;
import com.quiz.javaquizapi.dao.ResourceRepository;
import com.quiz.javaquizapi.model.file.Resource;
import com.quiz.javaquizapi.model.file.ResourceFolder;
import com.quiz.javaquizapi.model.file.ResourceStatus;
import com.quiz.javaquizapi.model.profile.Profile;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import java.util.Objects;
import java.util.UUID;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

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
        createResources(3, ResourceStatus.APPROVED, getSecondLayer());
        createResources(2, ResourceStatus.READY_FOR_REVIEW, getRoot());
        createResources(5, ResourceStatus.APPROVED, getFirstLayer());
        createResources(3, ResourceStatus.SAVED);
    }

    @Test
    @DisplayName("Fetch approved page")
    public void testFetchingApprovedGivenPageParams() {
        var approved = resourceRepository.findAllApproved(Pageable.ofSize(PAGE_SIZE));
        assertThat(approved).isNotNull();
        assertThat(approved.get().count()).isEqualTo(8);
        assertThat(approved.get().map(Resource::getStatus)).allMatch(ResourceStatus.APPROVED::equals);
        assertThat(approved.get().map(Resource::getApprovedBy)).allMatch(localProfile::equals);
        assertThat(approved.get().map(Resource::getFolder).filter(getFirstLayer()::equals).count())
                .isEqualTo(5);
        assertThat(approved.get().map(Resource::getFolder).filter(getSecondLayer()::equals).count())
                .isEqualTo(3);
        assertExecutedQueries();
    }

    @Test
    @DisplayName("Fetch ready for review")
    public void testFetchingReadyForReviewGivenPageParams() {
        var resources = resourceRepository.findAllReadyForReview(Pageable.ofSize(PAGE_SIZE));
        assertThat(resources).isNotNull();
        assertThat(resources.get().count()).isEqualTo(4);
        assertThat(resources.get().map(Resource::getStatus)).allMatch(ResourceStatus.READY_FOR_REVIEW::equals);
        assertExecutedQueries();
    }

    @Test
    @DisplayName("Fetch saved page")
    public void testFetchingSavedGivenPageParams() {
        var saved = resourceRepository.findByStatus(ResourceStatus.SAVED, Pageable.ofSize(PAGE_SIZE));
        assertThat(saved).isNotNull();
        assertThat(saved.get().count()).isEqualTo(3);
        assertThat(saved.get().map(Resource::getStatus)).allMatch(ResourceStatus.SAVED::equals);
        assertThat(saved.get().map(Resource::getApprovedBy)).allMatch(Objects::isNull);
        assertThat(saved.get().map(Resource::getFolder)).allMatch(getRoot()::equals);
        assertExecutedQueries();
    }

    @Test
    @DisplayName("Fetch by approver")
    public void testFetchingApprovedGivenApproverCode() {
        var approved = resourceRepository.findByApprovedByCode(localProfile.getCode(), Pageable.ofSize(PAGE_SIZE));
        assertThat(approved).isNotNull();
        assertThat(approved.get().count()).isEqualTo(8);
        assertThat(approved.get().map(Resource::getStatus)).allMatch(ResourceStatus.APPROVED::equals);
        assertThat(approved.get().map(Resource::getApprovedBy)).allMatch(localProfile::equals);
        assertThat(approved.get().map(Resource::getFolder).filter(getFirstLayer()::equals).count())
                .isEqualTo(5);
        assertThat(approved.get().map(Resource::getFolder).filter(getSecondLayer()::equals).count())
                .isEqualTo(3);
        assertExecutedQueries();
    }

    @Test
    @DisplayName("Fetch by creator code")
    public void testFetchingResourcesGivenCreatorCode() {
        var resources = resourceRepository.findByCreatedByCode(localProfile.getCode(), Pageable.ofSize(PAGE_SIZE));
        assertThat(resources).isNotNull();
        assertThat(resources.get().count()).isEqualTo(15);
        assertThat(resources.get()
                .map(Resource::getStatus)
                .filter(ResourceStatus.APPROVED::equals)
                .count())
                .isEqualTo(8);
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
        assertThat(resources.get().map(Resource::getCreatedBy)).allMatch(localProfile::equals);
        assertExecutedQueries();
    }

    @Test
    @DisplayName("Fetch by folder path")
    public void testFetchingResourceGivenFolderPath() {
        var resources = resourceRepository.findByFolderPath(getSecondLayer().getPath(), Pageable.ofSize(PAGE_SIZE));
        assertThat(resources).isNotNull();
        assertThat(resources.get().count()).isEqualTo(3);
        assertThat(resources.get().map(Resource::getStatus)).allMatch(ResourceStatus.APPROVED::equals);
        assertThat(resources.get().map(Resource::getFolder)).allMatch(getSecondLayer()::equals);
        assertExecutedQueries();
    }

    @Test
    @DisplayName("Delete a resource")
    public void testDeletingResourceGivenValidCode() {
        var resource = initResource(START_INCLUSIVE, ResourceStatus.SAVED, getRoot());
        resourceRepository.save(resource);
        resourceRepository.findByCode(resource.getCode())
                .ifPresentOrElse(res -> assertThat(resourceRepository.deleteByCode(resource.getCode()))
                                .isEqualTo(1),
                        () -> Assertions.fail("Unable to delete a resource"));
        assertExecutedQueries(ExecutedQueries.TWO);
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
        var resource = new Resource().setCreatedBy(localProfile).setResourceId(String.valueOf(index)).setStatus(status)
                .setFolder(parent);
        resource.setCode(UUID.randomUUID().toString());
        if (ResourceStatus.APPROVED.equals(status)) {
            resource.setApprovedBy(localProfile);
        }
        return resource;
    }
}
