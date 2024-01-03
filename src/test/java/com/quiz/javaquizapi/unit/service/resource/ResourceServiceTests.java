package com.quiz.javaquizapi.unit.service.resource;

import com.quiz.javaquizapi.common.util.ValidationUtils;
import com.quiz.javaquizapi.dao.ResourceRepository;
import com.quiz.javaquizapi.exception.file.ResourceNotFoundException;
import com.quiz.javaquizapi.model.file.Resource;
import com.quiz.javaquizapi.model.file.ResourceStatus;
import com.quiz.javaquizapi.service.BaseQuizService;
import com.quiz.javaquizapi.service.file.ResourceService;
import com.quiz.javaquizapi.service.file.impl.QuizResourceService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Resource service tests")
public class ResourceServiceTests extends ResourceTests {
    private Resource localResource = new Resource()
            .setResourceId(ID)
            .setCreatedBy(getLocalProfile());

    {
        localResource.setCode(UUID.randomUUID().toString());
    }

    @Mock
    private ResourceRepository repository;
    private ResourceService service;

    @Override
    @BeforeEach
    protected void setUp() {
        super.setUp();
        service = new QuizResourceService(repository);
    }

    @Test
    @DisplayName("Fetch resource by its code")
    public void testFetchingResourceGivenValidCode() {
        when(repository.findByCode(localResource.getCode())).thenReturn(Optional.of(localResource));
        var actual = service.get(localResource.getCode());
        verify(repository).findByCode(localResource.getCode());
        assertThat(actual).isNotNull();
        assertThat(actual.getCode()).isEqualTo(localResource.getCode());
        assertThat(actual.getCreatedBy()).isEqualTo(localResource.getCreatedBy());
        assertThat(actual.getResourceId()).isEqualTo(localResource.getResourceId());
        assertThat(captureLogs()).contains("Fetching a Resource by code...");
    }

    @Test
    @DisplayName("Fetch resource by invalid code")
    public void testFetchingResourceGivenInvalidCode() {
        var exception = Assertions.assertThrows(ResourceNotFoundException.class, () -> service.get("wrong code"));
        verify(repository).findByCode("wrong code");
        assertThat(exception).isNotNull();
        assertThat(exception.getReason()).isEqualTo(ResourceNotFoundException.DEFAULT_ERROR);
        assertThat(exception.getCode()).isEqualTo(ResourceNotFoundException.RESOURCE_NOT_FOUND_CODE);
        assertThat(exception.getArgs()).hasSize(0);
        assertThat(captureLogs()).contains("Fetching a Resource by code...");
    }

    @Test
    @DisplayName("Fetch approved resources")
    public void testFetchingApprovedResourceGivenValidPage() {
        when(repository.findAllApproved(any(Pageable.class)))
                .thenReturn(generateTestResources(3, ResourceStatus.APPROVED));
        var resources = service.getApproved(service.requestPage());
        verify(repository).findAllApproved(any(Pageable.class));
        assertThat(resources).isNotNull();
        assertThat(resources.get().count()).isEqualTo(3);
        assertTrue(resources.get().map(Resource::getStatus).allMatch(ResourceStatus.APPROVED::equals));
        assertThat(captureLogs()).contains("Fetching a Resource by status...");
    }

    @Test
    @DisplayName("Fetch ready for review resources")
    public void testFetchingReadyForReviewResourceGivenValidPage() {
        when(repository.findAllReadyForReview(any(Pageable.class)))
                .thenReturn(generateTestResources(3, ResourceStatus.READY_FOR_REVIEW));
        var resources = service.getReadyForReview(service.requestPage());
        verify(repository).findAllReadyForReview(any(Pageable.class));
        assertThat(resources).isNotNull();
        assertThat(resources.get().count()).isEqualTo(3);
        assertTrue(resources.get().map(Resource::getStatus).allMatch(ResourceStatus.READY_FOR_REVIEW::equals));
        assertThat(captureLogs()).contains("Fetching a Resource by status...");
    }

    @Test
    @DisplayName("Fetch saved resources")
    public void testFetchingSavedResourceGivenValidPage() {
        when(repository.findByStatus(eq(ResourceStatus.SAVED), any(Pageable.class)))
                .thenReturn(generateTestResources(3, ResourceStatus.SAVED));
        var resources = service.getByStatus(ResourceStatus.SAVED, service.requestPage());
        verify(repository).findByStatus(eq(ResourceStatus.SAVED), any(Pageable.class));
        assertThat(resources).isNotNull();
        assertThat(resources.get().count()).isEqualTo(3);
        assertTrue(resources.get().map(Resource::getStatus).allMatch(ResourceStatus.SAVED::equals));
        assertThat(captureLogs()).contains("Fetching a Resource by status...");
    }

    @Test
    @DisplayName("Fetch resources by approver")
    public void testFetchingResourceByApproverGivenValidCode() {
        when(repository.findByApprovedByCode(eq(getLocalProfile().getCode()), any(Pageable.class)))
                .thenReturn(generateTestResources(3, ResourceStatus.APPROVED));
        var resources = service.getByApprover(getLocalProfile().getCode(), service.requestPage());
        verify(repository).findByApprovedByCode(eq(getLocalProfile().getCode()), any(Pageable.class));
        assertThat(resources).isNotNull();
        assertThat(resources.get().count()).isEqualTo(3);
        assertTrue(resources.get().map(Resource::getStatus).allMatch(ResourceStatus.APPROVED::equals));
        assertTrue(resources.get().map(Resource::getApprovedBy).allMatch(getLocalProfile()::equals));
        assertThat(captureLogs()).contains("Fetching a Resource by approver...");
    }

    @Test
    @DisplayName("Fetch resource by creator")
    public void testFetchingResourceByCreatorGivenValidCode() {
        when(repository.findByCreatedByCode(eq(getLocalProfile().getCode()), any(Pageable.class)))
                .thenReturn(generateTestResources(3, ResourceStatus.IN_REVIEW));
        var resources = service.getByCreator(getLocalProfile().getCode(), service.requestPage());
        verify(repository).findByCreatedByCode(eq(getLocalProfile().getCode()), any(Pageable.class));
        assertThat(resources).isNotNull();
        assertThat(resources.get().count()).isEqualTo(3);
        assertTrue(resources.get().map(Resource::getStatus).allMatch(ResourceStatus.IN_REVIEW::equals));
        assertTrue(resources.get().map(Resource::getCreatedBy).allMatch(getLocalProfile()::equals));
        assertThat(captureLogs()).contains("Fetching a Resource by creator...");
    }

    @Test
    @DisplayName("Fetch resource by folder path")
    public void testFetchingResourceByFolderPathGivenValidPath() {
        var path = "folder1.folder2";
        when(repository.findByFolderPath(eq(path), any(Pageable.class)))
                .thenReturn(generateTestResources(4, ResourceStatus.APPROVED));
        var resources = service.getByPath(path, service.requestPage());
        verify(repository).findByFolderPath(eq(path), any(Pageable.class));
        assertThat(resources).isNotNull();
        assertThat(resources.get().count()).isEqualTo(4);
        assertTrue(resources.get().map(Resource::getStatus).allMatch(ResourceStatus.APPROVED::equals));
        assertThat(captureLogs()).contains("Fetching a Resource by folder path...");
    }

    @Test
    @DisplayName("Create a new resource")
    public void testCreatingResourceGivenValidData() {
        var expected = new Resource()
                .setResourceId(ID)
                .setCreatedBy(getLocalProfile());
        service.create(expected);
        var resourceCaptor = ArgumentCaptor.forClass(Resource.class);
        verify(repository).save(resourceCaptor.capture());
        var actual = resourceCaptor.getValue();
        assertThat(actual).isNotNull();
        assertThat(actual.getCreatedBy()).isEqualTo(expected.getCreatedBy());
        assertThat(actual.getResourceId()).isEqualTo(expected.getResourceId());
        assertThat(ValidationUtils
                .validator(BaseQuizService.CODE_ERROR_FORMAT.formatted(Resource.class.getSimpleName()))
                .validateCode(actual.getCode()))
                .isNotNull();
        assertThat(actual.getStatus()).isEqualTo(ResourceStatus.SAVED);
        assertThat(captureLogs()).contains("Saving a resource...");
    }

    @Test
    @DisplayName("Update a resource")
    public void testUpdatingResourceGivenValidData() {
        var expected = new Resource()
                .setStatus(ResourceStatus.APPROVED)
                .setResourceId(ID)
                .setCreatedBy(getLocalProfile());
        service.update(expected);
        assertThat(captureLogs()).contains("Saving an updated resource...");
    }

    @Test
    @DisplayName("Delete a resource")
    public void testDeletingResourceGivenValidCode() {
        when(repository.deleteByCode(localResource.getCode())).thenReturn(1);
        assertThat(service.delete(localResource.getCode())).isTrue();
        assertThat(captureLogs()).contains("Deleting a resource...");
    }

    @Test
    @DisplayName("Delete all accepted resources")
    public void testDeletingResourcesGivenResourcesPage() {
        var resources = generateTestResources(25, ResourceStatus.SAVED);
        doNothing().when(repository).deleteAll(resources.toList());
        assertThat(service.deleteBatch(resources)).isTrue();
        assertThat(captureLogs()).contains("Deleting a batch of resources...");
        verify(repository).deleteAll(resources.toList());
    }
}
