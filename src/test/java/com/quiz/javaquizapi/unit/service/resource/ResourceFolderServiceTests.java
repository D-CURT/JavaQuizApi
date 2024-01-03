package com.quiz.javaquizapi.unit.service.resource;

import com.quiz.javaquizapi.ApiTests;
import com.quiz.javaquizapi.common.util.ValidationUtils;
import com.quiz.javaquizapi.dao.ResourceFolderRepository;
import com.quiz.javaquizapi.model.file.Resource;
import com.quiz.javaquizapi.model.file.ResourceFolder;
import com.quiz.javaquizapi.model.file.ResourceStatus;
import com.quiz.javaquizapi.service.BaseQuizService;
import com.quiz.javaquizapi.service.file.ResourceFolderService;
import com.quiz.javaquizapi.service.file.impl.QuizResourceFolderService;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.stream.IntStream;

import static com.quiz.javaquizapi.integration.dao.file.ResourceTests.PAGE_SIZE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Resource folder service tests")
public class ResourceFolderServiceTests extends ApiTests {
    @Mock
    private ResourceFolderRepository repository;
    private ResourceFolderService service;

    @BeforeEach
    void setUp() {
        initLog();
        service = new QuizResourceFolderService(repository);
    }

    @Test
    @DisplayName("Fetch folder by its path")
    public void testFetchingResourceFolderGivenValidPath() {
        var path = "test path";
        when(repository.findByPath(path)).thenReturn(Optional.of(new ResourceFolder().setPath(path)));
        var folder = service.getByPath(path);
        assertThat(folder).isNotNull();
        assertThat(folder.getPath()).isEqualTo(path);
        assertThat(captureLogs()).contains("Fetching a ResourceFolder by path...");
    }

    @Test
    @DisplayName("Fetch folders by parent code")
    public void testFetchingResourceFoldersGivenParentCode() {
        var parentCode = "parentCode";
        when(repository.findByParentCode(parentCode, Pageable.ofSize(PAGE_SIZE)))
                .thenReturn(
                        Optional.of(
                                generateTestResources(5, new ResourceFolder().setName(StringUtils.EMPTY))));
        var folders = service.getByParent(parentCode, Pageable.ofSize(PAGE_SIZE));
        assertThat(folders).isNotNull();
        assertThat(folders.get().count()).isEqualTo(5);
        assertThat(captureLogs()).contains("Fetching a ResourceFolder by parent...");
    }

    @Test
    @DisplayName("Create a new resource folder")
    public void testCreatingResourceFolderGivenValidData() {
        var expected = new ResourceFolder().setPath(StringUtils.EMPTY);
        service.create(expected);
        var folderCaptor = ArgumentCaptor.forClass(ResourceFolder.class);
        verify(repository).save(folderCaptor.capture());
        var actual = folderCaptor.getValue();
        assertThat(actual).isNotNull();
        assertThat(actual.getPath()).isEqualTo(expected.getPath());
        assertThat(ValidationUtils
                .validator(BaseQuizService.CODE_ERROR_FORMAT.formatted(ResourceFolder.class.getSimpleName()))
                .validateCode(actual.getCode())).isNotBlank();
        assertThat(captureLogs()).contains("Saving a resource folder...");
    }

    @Test
    @DisplayName("Update resource folder")
    public void testUpdatingResourceFolderGivenValidData() {
        ResourceFolder folder = new ResourceFolder().setPath("folder path");
        service.update(folder);
        assertThat(captureLogs()).contains("Saving an updated resource folder...");
        verify(repository).save(folder);
    }

    @Test
    @DisplayName("Delete resource folder by path")
    public void testDeletingResourceFolderGivenValidPath() {
        var path = "folder path";
        when(repository.deleteByPath(path)).thenReturn(1);
        assertThat(service.delete(path)).isTrue();
        assertThat(captureLogs()).contains("Deleting a resource folder by its path...");
        verify(repository).deleteByPath(path);
    }

    @AfterEach
    void tearDown() {
        clearLog();
    }

    private Page<ResourceFolder> generateTestResources(int quantity, ResourceFolder parent) {
        return service.getPage(IntStream.range(0, quantity)
                .mapToObj(num -> new ResourceFolder().setParent(parent).setName("folder" + ++num))
                .toList());
    }
}
