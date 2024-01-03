package com.quiz.javaquizapi.integration.facade.resource;

import com.quiz.javaquizapi.dto.resource.ResourceDto;
import com.quiz.javaquizapi.facade.mapping.Mapper;
import com.quiz.javaquizapi.facade.resource.QuizResourceFacade;
import com.quiz.javaquizapi.facade.resource.ResourceFacade;
import com.quiz.javaquizapi.model.file.Resource;
import com.quiz.javaquizapi.model.file.ResourceFolder;
import com.quiz.javaquizapi.model.file.ResourceStatus;
import com.quiz.javaquizapi.model.profile.Profile;
import com.quiz.javaquizapi.service.box.FileClient;
import com.quiz.javaquizapi.service.file.ResourceFolderService;
import com.quiz.javaquizapi.service.file.ResourceService;
import com.quiz.javaquizapi.service.me.profile.ProfileService;
import com.quiz.javaquizapi.unit.service.resource.ResourceTests;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

import static com.quiz.javaquizapi.common.util.GenericUtils.getPagedTypeOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Resource facade tests")
public class ResourceFacadeTests extends ResourceTests {
    public static final String TEST_ID = "resourceId";
    @Mock
    private ResourceService service;
    @Autowired
    private Mapper mapper;
    @Mock
    private ResourceFolderService folderService;
    @Mock
    private ProfileService profileService;
    @Mock
    private FileClient client;
    private ResourceFacade facade;

    @Override
    @BeforeEach
    protected void setUp() {
        super.setUp();
        initLog();
        facade = new QuizResourceFacade(service, mapper, folderService, profileService, client);
    }

    @Test
    @DisplayName("Download file")
    public void testDownloadingFileFromCloudGivenValidCode() {
        var code = "code";
        when(service.get(code)).thenReturn(new Resource().setResourceId(TEST_ID));
        var stream = new ByteArrayOutputStream();
        stream.writeBytes("content".getBytes());
        when(client.download(TEST_ID)).thenReturn(stream);
        var content = facade.download(code);
        assertThat(content).isNotNull();
    }

    @Test
    @DisplayName("Create resource")
    public void testCreatingResourceGivenValidData() {
        var parent = new ResourceFolder();
        parent.setCode(UUID.randomUUID().toString());
        var profile = new Profile().setUser(localUser);
        profile.setCode(UUID.randomUUID().toString());
        var file = new MockMultipartFile("test", "content".getBytes());
        when(profileService.getMe(localUser.getUsername())).thenReturn(profile);
        when(folderService.get(parent.getCode())).thenReturn(parent);
        when(client.upload(file)).thenReturn(TEST_ID);
        var expected = new ResourceDto().setFolderCode(parent.getCode());
        expected.setUsername(localUser.getUsername());
        var actual = facade.create(expected, file);
        var resourceCaptor = ArgumentCaptor.forClass(Resource.class);
        assertThat(actual).isNotNull();
        assertThat(actual.getResourceId()).isNull();
        assertThat(actual.getCreatedByCode()).isEqualTo(profile.getCode());
        assertThat(actual.getFolderCode()).isEqualTo(parent.getCode());
        assertThat(actual.getUsername()).isNull();
        verify(profileService).getMe(localUser.getUsername());
        verify(folderService).get(parent.getCode());
        verify(service).create(resourceCaptor.capture());
        verify(client).upload(file);
        var resource = resourceCaptor.getValue();
        assertThat(resource).isNotNull();
        assertThat(resource.getCreatedBy()).isEqualTo(profile);
        assertThat(resource.getResourceId()).isEqualTo(TEST_ID);
        assertThat(resource.getFolder()).isEqualTo(parent);
    }

    @Test
    @DisplayName("Approve resource")
    public void testApprovingResourceGivenValidData() {
        var expected = new ResourceDto();
        var profile = new Profile();
        profile.setCode(UUID.randomUUID().toString());
        expected.setUsername(localUser.getUsername());
        expected.setCode(UUID.randomUUID().toString());
        var resource = new Resource();
        resource.setCode(UUID.randomUUID().toString());
        when(profileService.getMe(localUser.getUsername())).thenReturn(profile);
        when(service.get(expected.getCode())).thenReturn(resource);
        facade.approve(expected);
        var resourceCaptor = ArgumentCaptor.forClass(Resource.class);
        verify(service).update(resourceCaptor.capture());
        var actual = resourceCaptor.getValue();
        assertThat(actual).isNotNull();
        assertThat(actual.getStatus()).isEqualTo(ResourceStatus.APPROVED);
        assertThat(actual.getApprovedBy()).isEqualTo(profile);
        assertThat(captureLogs()).contains("Approving a resource...");
        verify(profileService).getMe(localUser.getUsername());
        verify(service).get(expected.getCode());
    }

    @Test
    @DisplayName("Send to review a resource")
    public void testSendingToReviewResourceGivenValidData() {
        var resource = new Resource();
        resource.setCode(UUID.randomUUID().toString());
        when(service.get(resource.getCode())).thenReturn(resource);
        facade.sendToReview(resource.getCode());
        var resourceCaptor = ArgumentCaptor.forClass(Resource.class);
        verify(service).update(resourceCaptor.capture());
        var actual = resourceCaptor.getValue();
        assertThat(actual.getStatus()).isEqualTo(ResourceStatus.READY_FOR_REVIEW);
        assertThat(captureLogs()).contains("Sanding to review a resource...");
        verify(service).get(resource.getCode());
    }

    @Test
    @DisplayName("Review a resource")
    public void testReviewingResourceGivenValidData() {
        var resource = new Resource();
        resource.setCode(UUID.randomUUID().toString());
        when(service.get(resource.getCode())).thenReturn(resource);
        facade.review(resource.getCode());
        var resourceCaptor = ArgumentCaptor.forClass(Resource.class);
        verify(service).update(resourceCaptor.capture());
        var actual = resourceCaptor.getValue();
        assertThat(actual.getStatus()).isEqualTo(ResourceStatus.IN_REVIEW);
        assertThat(captureLogs()).contains("Starting review of resource...");
        verify(service).get(resource.getCode());
    }

    @Test
    @DisplayName("Fetch approved resources")
    public void testFetchingApprovedResourceGivenValidPageInfo() {
        final int QUANTITY = 5;
        var pageable = requestPage();
        when(service.getApproved(pageable))
                .thenReturn(generateTestResources(QUANTITY, ResourceStatus.APPROVED));
        var actual = facade.getApproved(pageable);
        assertThat(actual).isNotNull();
        assertThat(actual.get().count()).isEqualTo(QUANTITY);
        assertThat(actual.get().map(ResourceDto::getStatus)).allMatch(ResourceStatus.APPROVED::equals);
        assertThat(actual.get().map(ResourceDto::getCreatedByCode)).allMatch(getLocalProfile().getCode()::equals);
        assertThat(actual.get().map(ResourceDto::getApprovedByCode)).allMatch(getLocalProfile().getCode()::equals);
        verify(service).getApproved(pageable);
    }

    @Test
    @DisplayName("Fetch ready for review resources")
    public void testFetchingReadyForReviewResourceGivenValidPageInfo() {
        final int QUANTITY = 5;
        var pageable = requestPage();
        when(service.getReadyForReview(pageable))
                .thenReturn(generateTestResources(QUANTITY, ResourceStatus.READY_FOR_REVIEW));
        var actual = facade.getReadyForReview(pageable);
        assertThat(actual).isNotNull();
        assertThat(actual.get().count()).isEqualTo(QUANTITY);
        assertThat(actual.get().map(ResourceDto::getStatus)).allMatch(ResourceStatus.READY_FOR_REVIEW::equals);
        assertThat(actual.get().map(ResourceDto::getCreatedByCode)).allMatch(getLocalProfile().getCode()::equals);
        verify(service).getReadyForReview(pageable);
    }

    @Test
    @DisplayName("Fetch saved resources")
    public void testFetchingResourceGivenSavedStatus() {
        final int QUANTITY = 3;
        var pageable = requestPage();
        when(service.getByStatus(ResourceStatus.SAVED, pageable))
                .thenReturn(generateTestResources(QUANTITY, ResourceStatus.SAVED));
        var saved = facade.getByStatus(ResourceStatus.SAVED, pageable);
        assertThat(saved).isNotNull();
        assertThat(saved.get().count()).isEqualTo(QUANTITY);
        assertThat(saved.get().map(ResourceDto::getStatus)).allMatch(ResourceStatus.SAVED::equals);
        verify(service).getByStatus(ResourceStatus.SAVED, pageable);
    }

    @Test
    @DisplayName("Fetch resources in review")
    public void testFetchingResourceGivenInReviewStatus() {
        final int QUANTITY = 2;
        var pageable = requestPage();
        when(service.getByStatus(ResourceStatus.IN_REVIEW, pageable))
                .thenReturn(generateTestResources(QUANTITY, ResourceStatus.IN_REVIEW));
        var inReview = facade.getByStatus(ResourceStatus.IN_REVIEW, pageable);
        assertThat(inReview).isNotNull();
        assertThat(inReview.get().count()).isEqualTo(QUANTITY);
        assertThat(inReview.get().map(ResourceDto::getStatus)).allMatch(ResourceStatus.IN_REVIEW::equals);
        verify(service).getByStatus(ResourceStatus.IN_REVIEW, pageable);
    }

    @Test
    @DisplayName("Fetch resources by approver")
    public void testFetchingResourceGivenApproverCode() {
        final int QUANTITY = 4;
        var pageable = requestPage();
        when(service.getByApprover(getLocalProfile().getCode(), pageable))
                .thenReturn(generateTestResources(QUANTITY, ResourceStatus.APPROVED));
        var resources = facade.getByApprover(getLocalProfile().getCode(), pageable);
        assertThat(resources).isNotNull();
        assertThat(resources.get().count()).isEqualTo(QUANTITY);
        assertThat(resources.get().map(ResourceDto::getStatus)).allMatch(ResourceStatus.APPROVED::equals);
        assertThat(resources.get().map(ResourceDto::getApprovedByCode)).allMatch(getLocalProfile().getCode()::equals);
        verify(service).getByApprover(getLocalProfile().getCode(), pageable);
    }

    @Test
    @DisplayName("Fetch resources by creator")
    public void testFetchingResourceGivenValidCreatorCode() {
        final int QUANTITY = 3;
        var pageable = requestPage();
        when(service.getByCreator(getLocalProfile().getCode(), pageable))
                .thenReturn(generateTestResources(QUANTITY, ResourceStatus.SAVED));
        var resources = facade.getByCreator(getLocalProfile().getCode(), pageable);
        assertThat(resources).isNotNull();
        assertThat(resources.get().count()).isEqualTo(QUANTITY);
        assertThat(resources.get().map(ResourceDto::getStatus)).allMatch(ResourceStatus.SAVED::equals);
        assertThat(resources.get().map(ResourceDto::getCreatedByCode)).allMatch(getLocalProfile().getCode()::equals);
        verify(service).getByCreator(getLocalProfile().getCode(), pageable);
    }

    @Test
    @DisplayName("Fetch resources by path")
    public void testFetchingResourceGivenValidFolderPath() {
        final int QUANTITY = 7;
        var path = "folder1.folder2";
        var pageable = requestPage();
        when(service.getByPath(path, pageable))
                .thenReturn(generateTestResources(QUANTITY, ResourceStatus.APPROVED));
        var resources = facade.getByPath(path, pageable);
        assertThat(resources).isNotNull();
        assertThat(resources.get().count()).isEqualTo(QUANTITY);
        assertThat(resources.get().map(ResourceDto::getStatus)).allMatch(ResourceStatus.APPROVED::equals);
        verify(service).getByPath(path, pageable);
    }

    @Test
    @DisplayName("Delete resource")
    public void testDeletingResourceGivenValidCode() {
        var code = "code";
        facade.delete(code);
        verify(service).delete(code);
    }

    @Test
    @DisplayName("Create resource folder")
    public void testCreatingResourceFolderGivenValidData() {
        var name = "folderName";
        var parent = createParent();
        when(folderService.get(parent.getCode())).thenReturn(parent);
        facade.createFolder(parent.getCode(), name);
        var captor = ArgumentCaptor.forClass(ResourceFolder.class);
        verify(folderService).create(captor.capture());
        var folder = captor.getValue();
        assertThat(folder).isNotNull();
        assertThat(folder.getPath()).isEqualTo("parentName.folderName");
    }

    @Test
    @DisplayName("Rename resource folder")
    public void testRenamingResourceFolderGivenValidName() {
        var code = "folderCode";
        var parent = createParent();
        when(folderService.get(code))
                .thenReturn(new ResourceFolder().setParent(parent).setName("folderName"));
        facade.renameFolder(code, "newFolderName");
        var captor = ArgumentCaptor.forClass(ResourceFolder.class);
        verify(folderService).update(captor.capture());
        var folder = captor.getValue();
        assertThat(folder).isNotNull();
        assertThat(folder.getPath()).isEqualTo("parentName.newFolderName");
    }

    @Test
    @DisplayName("Move resource folder")
    public void testMovingResourceFolderGivenValidParent() {
        var code = "folderCode";
        var parent = createParent();
        when(folderService.get(code))
                .thenReturn(new ResourceFolder().setParent(parent).setName("folderName"));
        when(folderService.get(parent.getCode()))
                .thenReturn(new ResourceFolder().setParent(parent).setName("newParent"));
        facade.moveFolder(code, parent.getCode());
        var captor = ArgumentCaptor.forClass(ResourceFolder.class);
        verify(folderService).update(captor.capture());
        var folder = captor.getValue();
        assertThat(folder).isNotNull();
        assertThat(folder.getPath()).isEqualTo("parentName.newParent.folderName");
    }

    @Test
    @DisplayName("Delete resource folder")
    public void testDeletingResourceFolderGivenValidPath() {
        var path = "path";
        var parent = createParent();
        var resources1 = mock(getPagedTypeOf(Resource.class));
        var resources2 = mock(getPagedTypeOf(Resource.class));
        var folders1 = mock(getPagedTypeOf(ResourceFolder.class));
        var folders2 = mock(getPagedTypeOf(ResourceFolder.class));
        var folder = new ResourceFolder().setParent(parent).setName("folderName");
        folder.setCode(UUID.randomUUID().toString());
        when(folderService.getByPath(path)).thenReturn(folder);
        when(service.getByPath(folder.getPath(), service.requestPage()))
                .thenReturn(resources1)
                .thenReturn(resources2);
        when(resources1.isFirst()).thenReturn(Boolean.TRUE);
        when(resources1.hasNext()).thenReturn(Boolean.TRUE);
        when(resources2.hasNext()).thenReturn(Boolean.FALSE);
        when(folderService.getByParent(folder.getCode(), folderService.requestPage()))
                .thenReturn(folders1)
                .thenReturn(folders2);
        when(folders1.isFirst()).thenReturn(Boolean.TRUE);
        when(folders1.hasNext()).thenReturn(Boolean.TRUE);
        when(folders2.hasNext()).thenReturn(Boolean.FALSE);
        facade.deleteFolder(path);
        assertThat(captureLogs())
                .contains("Deleting all the resources within path 'parentName.folderName'...",
                        "Deleting child folders recursively...",
                        "Removing files from the cloud...",
                        "Removing resources form the DB...",
                        "Folder 'parentName.folderName' fully removed.");
    }

    @AfterEach
    void tearDown() {
        clearLog();
    }

    private ResourceFolder createParent() {
        var root = new ResourceFolder().setName(StringUtils.EMPTY);
        var folder = new ResourceFolder().setParent(root).setName("parentName");
        folder.setCode(UUID.randomUUID().toString());
        return folder;
    }
}
